from agno.tools import Toolkit
from httpx import AsyncClient
from fastapi import HTTPException
from pydantic import BaseModel, Field
from typing import Optional, List
import os
from dotenv import load_dotenv

load_dotenv()
BACKEND_CORE = os.getenv("BACKEND_CORE")


class Vehicle(BaseModel):
    id: int
    status: str
    park_id: int | None = None
    reports: list[str] | None = None


class VehicleList(BaseModel):
    vehicles: list[Vehicle]


class MaintenanceVehicle(BaseModel):
    id: int
    immissionDate: str
    dismissionDate: Optional[str] = None
    vehicleType: str


class Maintenance(BaseModel):
    id: int = Field(alias="idMaintenance")
    vehicle: MaintenanceVehicle
    description: str
    start: str
    end: Optional[str] = None


class Suspension(BaseModel):
    start: str
    end: Optional[str]
    value: float
    paid: bool
    status: Optional[str]


class User(BaseModel):
    username: str
    name: str
    email: str
    suspendedAt: str
    toPay: str


class ManagerTools(Toolkit):
    def __init__(self, token: str, **kwargs):
        super().__init__(
            name="full_manager_agent",
            tools=[
                self.add_vehicle,
                self.get_vehicle_list_with_reports,
                self.start_maintenance,
                self.end_maintenance,
                self.get_maintaining_vehicles,
            ],
            **kwargs,
        )
        self.token = token

    async def add_vehicle(self, park_id: int, vehicle_type: int) -> None:
        async with AsyncClient() as client:
            response = await client.post(
                f"http://{BACKEND_CORE}/parks/{park_id}/vehicle",
                headers={"Authorization": self.token},
                params={"vehicleType": vehicle_type},
            )
        if response.status_code != 200:
            raise HTTPException(status_code=response.status_code, detail=response.text)
            # Non fare response.json() perché il body è vuoto
        return None

    async def get_vehicle_list_with_reports(self) -> list[dict]:
        async with AsyncClient() as client:
            result = await client.get(
                f"http://{BACKEND_CORE}/parks/vehiclesWithStatusAndReports",
                headers={"Authorization": self.token},
            )
        if result.status_code != 200:
            raise HTTPException(status_code=result.status_code, detail=result.text)
        return result.json()

    async def start_maintenance(
        self, id_vehicle: int, description: str = "prova"
    ) -> Maintenance:
        async with AsyncClient() as client:
            response = await client.post(
                f"http://{BACKEND_CORE}/vehicles/maintenance",
                headers={"Authorization": self.token},
                params={"vehicleId": id_vehicle, "description": description},
            )
        if response.status_code != 200:
            raise HTTPException(status_code=response.status_code, detail=response.text)
        return

    async def end_maintenance(self, id_vehicle: int, id_park: int) -> Maintenance:
        async with AsyncClient() as client:
            response = await client.post(
                f"http://{BACKEND_CORE}/vehicles/maintenance/end",
                headers={"Authorization": self.token},
                params={"vehicleId": id_vehicle, "parkId": id_park},
            )
        if response.status_code != 200:
            raise HTTPException(status_code=response.status_code, detail=response.text)
        return

    async def get_maintaining_vehicles(self) -> List[Maintenance]:
        """This method returns the maintaining vehicles, the agent should consider as
        resolved any maintaining with a date of end (do not consider those as actual mantainances)"""
        async with AsyncClient() as client:
            result = await client.get(
                f"http://{BACKEND_CORE}/vehicles/maintenance",
                headers={"Authorization": self.token},
            )
        if result.status_code != 200:
            raise HTTPException(status_code=result.status_code, detail=result.text)
        return [Maintenance(**v) for v in result.json()]
