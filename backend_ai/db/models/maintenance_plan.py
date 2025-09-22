from beanie import Document


class MaintenancePlan(Document):
    # TODO add MaintenancePlan document fields from MongoDB
    class Settings:
        name = "MaintenancePlan"

    class Config:
        # TODO add json schema for swagger OpenApi
        json_schema_extra = {
            "example": {
                "test": 232,
            }
        }
