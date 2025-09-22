import os

from dotenv import load_dotenv
from fastapi import HTTPException, Header, status
from httpx import AsyncClient, codes

load_dotenv()
BACKEND_CORE = os.getenv("BACKEND_CORE")


async def verify_admin(authorization: str = Header(..., alias="Authorization")):
    return await generic_verify("admin", authorization)


async def verify_manager(authorization: str = Header(..., alias="Authorization")):
    return await generic_verify("manager", authorization)


async def verify_customer(authorization: str = Header(..., alias="Authorization")):
    return await generic_verify("customer", authorization)


async def generic_verify(
    role: str, authorization: str = Header(..., alias="Authorization")
):
    if not authorization.startswith("Bearer "):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Formato Authorization non valido",
        )

    async def is_valid_token(token: str) -> bool:
        async with AsyncClient() as client:
            result = await client.get(
                f"http://{BACKEND_CORE}/auth/{role}-login",
                headers={"Authorization": token},
            )
            return result.status_code == codes.OK

    if not await is_valid_token(authorization):
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED, detail="Token non valido"
        )
    return authorization
