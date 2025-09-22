import os

from agno.agent import Agent
from agno.memory.v2.db.mongodb import MongoMemoryDb
from agno.memory.v2.memory import Memory
from agno.models.google import Gemini
from agno.storage.mongodb import MongoDbStorage
from agno.tools.reasoning import ReasoningTools
from agno.tools.thinking import ThinkingTools
from dotenv import load_dotenv

from .tools.user_tools import UserTools

load_dotenv()
DEFAULT_GEMINI_MODEL = os.getenv("GEMINI_MODEL_ID", "gemini-2.5-flash-lite")

memory = Memory(
    model=Gemini(id=DEFAULT_GEMINI_MODEL),
    db=MongoMemoryDb(
        db_url=os.getenv("MONGO_URI"),
        db_name="MobiShare",
        collection_name="UserMemory",
    ),
)

storage = MongoDbStorage(
    db_url=os.getenv("MONGO_URI"),
    db_name="MobiShare",
    collection_name="UserSessions",
)

def get_user_agent(username: str, token: str) -> Agent:
    print("NEW AGENT")
    return Agent(
        model=Gemini(id=DEFAULT_GEMINI_MODEL),
        tools=[
            UserTools(token, username),
            ReasoningTools(think=True, analyze=True),
            ThinkingTools(think=True),
        ],
        user_id=username,
        instructions=[
            "You speak both italian and English",
            "You are the user support for his operation and daily task: step by step app guide, making statistics on the user data",
            "you need to be coincise and clear."
            "you are the AI assistant of Mobishare, a bike and scooter sharing app, you will help the user to use the app and "
            "you can also make statistics on the user data",
            "You must not invent any information, if you don't know the answer to a question, you must say 'I don't know',"
            "each information must be retrieved from the tools you have access to, if the prompt asks for help with the app, "
            "you must use the tools starting with 'how_to' as a tutorial for the user on ask for help",
            "you must use the tools starting with 'action' in case the user is asking you to do something for him (like retrieve data or something more concrete)"
            "You must respond with a printable string, without escapes or special characters",
        ],
        memory=memory,
        storage=storage,

        enable_agentic_memory=True,
        add_history_to_messages=True,
        markdown=True,
    )
