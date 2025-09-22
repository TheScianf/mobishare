import os
from agno.agent import Agent
from agno.memory.v2.db.mongodb import MongoMemoryDb
from agno.memory.v2.memory import Memory
from agno.storage.mongodb import MongoDbStorage
from agno.models.google import Gemini
from agno.tools.thinking import ThinkingTools
from agno.tools.reasoning import ReasoningTools
from .tools.admin_tools import AdminTools
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
        collection_name="AdminMemory",
    ),
)
storage = MongoDbStorage(
    collection_name="AdminSessions",
    db_name="MobiShare",
    db_url=MONGO_URI,
)


def get_admin_agent(admin: str, token: str) -> Agent:
    return Agent(
        model=Gemini(id=DEFAULT_GEMINI_MODEL),
        tools=[
            AdminTools(token),
            ManagerTools(token),
            ThinkingTools(think=True),
            ReasoningTools(think=True, analyze=True),
        ],
        user_id=admin,
        session_id=admin,
        instructions=[
            "Role: Administrative assistant for MobiShare operations (parking lot, users, managers, vehicle and suspended user).",
            "Language: reply in Italian if the user writes in Italian; otherwise use clear English.",
            "Style: be concise, use short bullet points, include assumptions and required inputs.",
            "Use tools for data before answering. If a tool fails, say what failed and suggest a next step.",
            "Status rule: a suspended user WITH an end date is considered active for current-status checks; include such suspensions when asked for suspension history.",
            "Output: no speculation; if information is missing, ask for the minimal field (e.g., user email/ID). Dates in ISO-8601 (YYYY-MM-DD).",
        ],
        memory=memory,
        storage=storage,
        add_history_to_messages=True,
        enable_agentic_memory=True,
        markdown=True,
    )
