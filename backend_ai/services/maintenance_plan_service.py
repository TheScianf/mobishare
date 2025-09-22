import json

from google import genai
from pydantic import BaseModel

from ..db.models.feedback import Feedback


class PlanSchema(BaseModel):
    state: int
    summary: str
    priorities: list[int]


class MaintenancePlanService:
    async def get_maintenance_plan(
        feedback: list[Feedback], google_client: genai.Client
    ):
        response = google_client.models.generate_content(
            model="gemini-2.5-flash-lite",
            config={
                "response_mime_type": "application/json",
                "response_schema": PlanSchema,
            },
            contents=f"Give me a maintenance plan for a car park based on these vehicle feedback: {feedback}, rate the state of the park from 1 to 3(higher number, higher need of maintenace), give me a brief summary and then in wwhic order send the vehicle to maintenance. In italian",
        )
        plan_json = json.loads(response.text)
        return PlanSchema(**plan_json)
