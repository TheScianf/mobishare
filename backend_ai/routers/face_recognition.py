from fastapi import APIRouter, UploadFile, File, HTTPException, Header
from ..services.face_recognition_service import FaceRecognitionService

router = APIRouter(tags=["Face recognition"], prefix="/face_recognition")


@router.post("/register")
async def save_face_encoding(
    username: str = Header(), image: UploadFile = File(...)
) -> dict:
    result = await FaceRecognitionService.save_encoding(username, image)

    if result:
        return {"message": "Face encoding saved successfully", "status": "success"}
    else:
        raise HTTPException(
            status_code=400,
            detail="Failed to save face encoding. Please ensure the image contains exactly one face.",
        )


@router.post("/verify")
async def login_with_face(image: UploadFile = File(...)) -> dict:
    result = await FaceRecognitionService.recognize_face(image)

    if result:
        return {"message": "Face recognized", "status": "success"}
    else:
        raise HTTPException(
            status_code=400,
            detail="Failed to save face encoding. Please ensure the image contains exactly one face or that you're registered.",
        )
