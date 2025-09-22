import os

from fastapi import FastAPI

from motor.motor_asyncio import AsyncIOMotorClient
from beanie import init_beanie
import logging
from .routers import face_recognition, smart_assistant, mobi_agent
from .db.models.feedback import Feedback
from .db.models.user_face import Face
from dotenv import load_dotenv

logger = logging.getLogger("uvicorn.error")
load_dotenv()

async def db_lifespan(app: FastAPI):
    # Startup
    app.mongodb_client = AsyncIOMotorClient(os.getenv("MONGO_URI"))
    app.database = app.mongodb_client["MobiShare"]
    ping_response = await app.database.command("ping")
    if int(ping_response["ok"]) != 1:
        raise Exception("Problem connecting to database cluster.")
    else:
        logger.info("Succesfully connected to MobiShare Database")
        await init_beanie(database=app.database, document_models=[Feedback, Face])
    yield

    # Shutdown
    app.mongodb_client.close()


app = FastAPI(
    lifespan=db_lifespan,
    title="MobiShare AI",
    summary="All the AI functionalities available for Mobishare core services",
)

app.include_router(face_recognition.router)
app.include_router(smart_assistant.router)
app.include_router(mobi_agent.router)
