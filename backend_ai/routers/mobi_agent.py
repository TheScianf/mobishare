from dotenv import load_dotenv
from fastapi import APIRouter, Depends
from pydantic import BaseModel

from ..dependencies import verify_admin, verify_customer, verify_manager
from ..services.mobi_agents.admin_agent import get_admin_agent
from ..services.mobi_agents.manager_agent import get_manager_agent

from ..services.mobi_agents.user_agent import get_user_agent

router = APIRouter(tags=["Agent Systems"], prefix="/chat")

class UserPromptRequest(BaseModel):
    username: str
    prompt: str

class ManagerPromptRequest(BaseModel):
    username: str
    prompt: str

user_agent = None
manager_agent = None

class UserPromptRequest(BaseModel):
    username: str
    prompt: str


class ManagerPromptRequest(BaseModel):
    username: str
    prompt: str


class AdminPromptRequest(BaseModel):
    username: str
    prompt: str


user_agent = None
# manager_agent = None


@router.post("/user")
async def prompt_user_agent(
    payload: UserPromptRequest, auth=Depends(verify_customer)
) -> None:
    global user_agent
    if user_agent is None:
        user_agent = get_user_agent(payload.username, auth)

    response = await user_agent.arun(payload.prompt)
    return response.content


@router.post("/admin")
async def prompt_admin_agent(
    payload: AdminPromptRequest, auth=Depends(verify_admin)
) -> None:
    agent = get_admin_agent(payload.username, auth)
    response = await agent.arun(payload.prompt)
    return response.content


@router.post("/manager")
async def prompt_manager_agent(
    payload: ManagerPromptRequest, auth=Depends(verify_manager)
) -> None:
    manager_agent = get_manager_agent(payload.username, auth)
    response = await manager_agent.arun(payload.prompt)
    return response.content
