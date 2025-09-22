import json

from google import genai
from pydantic import BaseModel

from ..db.models.feedback import Feedback


class FeedbackSchema(BaseModel):
    id_vehicle: int
    rating: int
    bullet_points: list[str]


class FeedbackService:
    async def get_feedback(
        rating: int = None, id_vehicle: int = None
    ) -> list[Feedback]:
        if rating and id_vehicle:
            search_criteria = {"rating": rating, "id_vehicle": id_vehicle}
        elif rating:
            search_criteria = {"rating": rating}
        elif id_vehicle:
            search_criteria = {"id_vehicle": id_vehicle}
        else:
            return await Feedback.find_all().to_list()
        feedback = await Feedback.find(search_criteria).to_list()
        return feedback

    async def format_feedback(
        feedback: str, id_vehicle: int, google_client: genai.Client
    ) -> Feedback:
        response = google_client.models.generate_content(
            model="gemini-2.5-flash-lite",
            config={
                "response_mime_type": "application/json",
                "response_schema": FeedbackSchema,
            },
            contents=f"Format and rate this feedback(from 1 to 3, highest value highest priority), the formatting should be coincise and in Italian: id:{id_vehicle} {feedback}",
        )
        feedback_json = json.loads(response.text)
        feedback = Feedback(**feedback_json)
        await feedback.insert()
        return feedback

    async def delete_feedback(id_vehicle: int):
        await Feedback.find(Feedback.id_vehicle == id_vehicle).delete()
