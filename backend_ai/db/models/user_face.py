from beanie import Document


class Face(Document):
    username: str

    encoding: list[float]

    class Settings:
        name = "Face"

    class Config:
        json_schema_extra = {
            "example": {
                "id_face": 357,
                "encoding": [
                    -0.13110043108463287,
                    0.19034768640995026,
                    0.05122075229883194,
                    -0.035913825035095215,
                    -0.06805745512247086,
                ],
            }
        }
