from agno.tools import Toolkit
from pydantic import BaseModel
from httpx import AsyncClient
from fastapi import HTTPException

import os
from dotenv import load_dotenv
import urllib.parse
from datetime import datetime, timezone

load_dotenv()
BACKEND_CORE = os.getenv("BACKEND_CORE")


class ToolAnswer(BaseModel):
    status: str
    message: str


class Manager(BaseModel):
    email: str
    id: int
    admin: bool


class Suspension(BaseModel):
    username: str
    email: str
    start_date: str
    end_date: str
    value: float
    paid: bool


class ManagerList(ToolAnswer):
    managers: list[Manager]


class ManagerCreated(ToolAnswer):
    email: str


class ManagerDeleted(ToolAnswer):
    email: str
    sub_email: str


class ManagerPromoted(ToolAnswer):
    manager_id: int


class SuspendedUserList(ToolAnswer):
    suspensions: list[Suspension]


class EndSuspension(ToolAnswer):
    username: str


class AdminTools(Toolkit):
    def __init__(self, token: str, **kwargs):
        super().__init__(
            name="admin_tools",
            tools=[
                self.get_manager_list,
                self.create_manager,
                self.delete_manager,
                self.move_vehicles,
                self.make_manager_admin,
                self.get_suspended_user_list,
                self.end_user_suspension,
            ],
            **kwargs,
        )
        self.token = token

    async def get_manager_list(self) -> ManagerList:
        """Get the list of manager emails from the Kotlin backend."""
        async with AsyncClient() as client:
            result = await client.get(
                f"http://{BACKEND_CORE}/managers",
                headers={"Authorization": self.token},
            )

        if result.status_code != 200:
            raise HTTPException(status_code=result.status_code, detail=result.text)

        try:
            managers_data = result.json()
        except Exception:
            raise HTTPException(status_code=500, detail="Invalid JSON from backend")

        managers = [
            Manager(email=manager["email"], id=manager["id"], admin=manager["admin"])
            for manager in managers_data
        ]
        return ManagerList(
            managers=managers,
            message="Correctly retrieved managers information",
            status="Success",
        )

    async def create_manager(
        self, email: str, password: str, admin: bool
    ) -> ManagerCreated:
        """Create a new manager that can have an admin role on the Kotlin backend."""
        async with AsyncClient() as client:
            result = await client.post(
                f"http://{BACKEND_CORE}/managers",
                headers={"Authorization": self.token},
                json={"email": email, "password": password, "admin": admin},
            )

        if result.status_code != 201:
            raise HTTPException(status_code=result.status_code, detail=result.text)

        return ManagerCreated(
            email=email,
            status="created",
            message=f"Manager with email {email} created successfully.",
        )

    async def delete_manager(self, email: str, sub_email: str) -> ManagerDeleted:
        """Delete a manager and substitute it with another one

        Args:
        email(str): email of the manager that will be deleted
        sub_email(str): email of the manager that will substitute the deleted one

        Returns:
            Status of the operation
        """
        async with AsyncClient() as client:
            result = await client.delete(
                f"http://{BACKEND_CORE}/managers/email",
                headers={"Authorization": self.token},
                params={"email": email, "subEmail": sub_email},
            )

        if result.status_code != 200:
            raise HTTPException(status_code=result.status_code, detail=result.text)

        return ManagerDeleted(
            email=email,
            sub_email=sub_email,
            status="deleted",
            message=f"Correctly deleted {email}, substitute it with {sub_email}",
        )

    async def make_manager_admin(self, manager_id: int) -> ManagerPromoted:
        """Give a manager the role of admin

        Args:
        manager(str): manager email

        Returns:
            Status of the operation
        """
        async with AsyncClient() as client:
            result = await client.put(
                f"http://{BACKEND_CORE}/managers/{manager_id}/toggleAdmin",
                headers={"Authorization": self.token},
            )

        if result.status_code != 200:
            raise HTTPException(status_code=result.status_code, detail=result.text)

        return ManagerPromoted(
            manager_id=manager_id,
            status="Success",
            message=f"Promotion succeded, manager with id{manager_id} is now an admin",
        )

    async def get_suspended_user_list(self) -> SuspendedUserList:
        """Retrieve a list of the current user that has a suspension

        Args:
        active(bool): flag that indicate an active suspension

        Returns:
            Currently suspended user
        """

        async with AsyncClient() as client:
            result = await client.get(
                f"http://{BACKEND_CORE}/suspensions",
                headers={"Authorization": self.token},
            )

        if result.status_code != 200:
            raise HTTPException(status_code=result.status_code, detail=result.text)

        try:
            suspensions_data = result.json()
        except Exception:
            raise HTTPException(status_code=500, detail="Invalid JSON from backend")

        suspensions = [
            Suspension(
                username=suspension["username"],
                email=suspension["email"],
                start_date=datetime.fromtimestamp(
                    suspension["start"] / 1000, tz=timezone.utc
                ).strftime("%Y-%m-%d"),
                end_date=datetime.fromtimestamp(
                    suspension["end"] / 1000, tz=timezone.utc
                ).strftime("%Y-%m-%d"),
                value=suspension["value"],
                paid=suspension["paid"],
            )
            for suspension in suspensions_data
        ]
        return SuspendedUserList(
            suspensions=suspensions,
            message="List of suspended users correctly retrieved",
            status="Success",
        )

    async def end_user_suspension(self, username: str, rejected: bool) -> EndSuspension:
        """Decide to end or permaban a user by specifying rejected value, true for permaban and false for ending the suspension

        Args:
        username(str)
        rejected(bool): True for permaban and false for ending the suspension

        Returns:
            Description of return value
        """
        async with AsyncClient() as client:
            result = await client.put(
                f"http://{BACKEND_CORE}/suspensions/byUser/{username}/last/isRejected",
                headers={"Authorization": self.token},
                json={"isRejected": rejected},
            )

        if result.status_code != 200:
            raise HTTPException(status_code=result.status_code, detail=result.text)

        return EndSuspension(
            username=username,
            status="Success",
            message=f"Correctly ended suspension for user {username}",
        )

    async def move_vehicles(self, id_vehicles: list[int], id_park: int) -> None:
        """Move vehicles to another car park

        Args:
        id_vehicles(list[int]): list of vehicles
        id_park(int): car park destination

        Returns:
            Status of the operation
        """
        async with AsyncClient() as client:
            result = await client.post(
                f"http://{BACKEND_CORE}/parks/vehicles/move",
                headers={"Authorization": self.token},
                params={"vehicleIds": id_vehicles, "parkId": id_park},
            )
