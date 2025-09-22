from beanie import Document


class Feedback(Document):
    id_vehicle: int
    rating: int
    bullet_points: list[str]

    class Settings:
        name = "Feedback"

    class Config:
        json_schema_extra = {
            "example": {
                "id_vehicle": 357,
                "rating": 3,
                "bullet_points": ["bullet1", "bullet2", "bullet3"],
            }
        }
