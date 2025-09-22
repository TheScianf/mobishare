import os
from agno.agent import Agent
from agno.memory.v2.db.mongodb import MongoMemoryDb
from agno.memory.v2.memory import Memory
from agno.storage.mongodb import MongoDbStorage
from agno.models.google import Gemini
from agno.tools.thinking import ThinkingTools
from agno.tools.reasoning import ReasoningTools
from .tools.manager_tools import ManagerTools
from dotenv import load_dotenv

load_dotenv()

DEFAULT_GEMINI_MODEL = os.getenv("GEMINI_MODEL_ID", "gemini-2.5-flash-lite")
MONGO_URI = os.getenv("MONGO_URI")

if not MONGO_URI:
    raise RuntimeError(
        "MONGO_URI is not set. Provide a valid MongoDB connection string in the environment."
    )

memory = Memory(
    model=Gemini(id=DEFAULT_GEMINI_MODEL),
    db=MongoMemoryDb(
        db_url=MONGO_URI,
        db_name="MobiShare",
        collection_name="ManagerMemory",
    ),
)
storage = MongoDbStorage(
    collection_name="ManagerSessions",
    db_name="MobiShare",
    db_url=MONGO_URI,
)


def get_manager_agent(manager: str, token: str) -> Agent:
    return Agent(
        model=Gemini(id=DEFAULT_GEMINI_MODEL),
        tools=[
            ManagerTools(token),
            ThinkingTools(think=True),
            ReasoningTools(think=True, analyze=True),
        ],
        user_id=manager,
        session_id=manager,
        instructions=[
            "Role: Operations manager assistant for a vehicle-sharing platform (vehicles, parking lot/car park).",
            "Language: answer in Italian if the user writes in Italian; otherwise use clear English.",
            "Style: concise bullets, surface assumptions and required inputs explicitly.",
            "Use tools to fetch current data before answering; if a tool fails, state the failure and propose the next step.",
            "Terminology: use 'parking lot' or 'car park' (avoid 'park').",
            "Suspension rule: if a suspension has an end date, do NOT treat it as currently active; still include it when the user asks for suspension history.",
            "Output: no speculation—ask for minimal missing identifiers (e.g., user ID/email, parking lot ID). Dates in ISO-8601 (YYYY-MM-DD).",
        ],
        memory=memory,
        storage=storage,
        add_history_to_messages=True,
        enable_agentic_memory=True,
        markdown=True,
    )
