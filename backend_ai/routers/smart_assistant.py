import os

from dotenv import load_dotenv
from fastapi import APIRouter, Depends
from google import genai
from pydantic import BaseModel

from ..db.models.feedback import Feedback
from ..dependencies import verify_manager
from ..services.feedback_service import FeedbackService
from ..services.maintenance_plan_service import MaintenancePlanService, PlanSchema

load_dotenv()

router = APIRouter(tags=["Smart Assistant"])
client = genai.Client(api_key=os.getenv("GOOGLE_API_KEY"))


@router.get("/feedback")
async def get_feedback_by_parameter(
    rating: int = None, id_vehicle: int = None
) -> list[Feedback]:
    return await FeedbackService.get_feedback(rating=rating, id_vehicle=id_vehicle)


@router.delete("/feedback", dependencies=[Depends(verify_manager)])
async def delete_feedback(id_vehicle: int = None):
    return await FeedbackService.delete_feedback(id_vehicle)


class FeedbackRequest(BaseModel):
    feedback: str
    id_vehicle: int


@router.post("/feedback/format")
async def format_feedback(request: FeedbackRequest) -> Feedback:
    response: Feedback = await FeedbackService.format_feedback(
        request.feedback, request.id_vehicle, client
    )
    return response


class MaintenancePlanRequest(BaseModel):
    id_vehicles: list[int]


@router.post("/maintenance/plan", dependencies=[Depends(verify_manager)])
async def generate_maintenance_plan(request: MaintenancePlanRequest) -> PlanSchema:
    feedback: list[Feedback] = []
    for id in request.id_vehicles:
        feedback.extend(await FeedbackService.get_feedback(None, id))
    return await MaintenancePlanService.get_maintenance_plan(feedback, client)
