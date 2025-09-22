from ..db.models.user_face import Face

import face_recognition as fr
from fastapi import UploadFile
import numpy as np

import tempfile
import os
# TODO refactor code like creating image encoding function


class FaceRecognitionService:
    async def save_encoding(username: str, image: UploadFile):
        img = await image.read()
        with open("./" + image.filename, mode="wb") as file:
            file.write(img)
        image_num = fr.load_image_file("./" + image.filename)

        if (encodings := fr.face_encodings(image_num)) and len(encodings) == 1:
            encoding = encodings[0].tolist()
            new_face = Face(username=username, encoding=encoding)
            await new_face.insert()
            return True
        else:
            return False

    # TODO implement function with temporary file instead of creating it and deleting
    # async def save_encoding(username: str, image: uploadfile):
    #     img = await image.read()
    #
    #     # Create a temporary file with the same extension as the uploaded file
    #     file_extension = os.path.splitext(image.filename)[1] if image.filename else ""
    #
    #     # Use delete=True (default) so the file is automatically deleted when closed
    #     with tempfile.NamedTemporaryFile(
    #         suffix=file_extension, delete=True
    #     ) as temp_file:
    #         temp_file.write(img)
    #         temp_file.flush()  # Ensure data is written to disk
    #
    #         # Load and process the image from the temporary file
    #         image_num = fr.load_image_file(temp_file.name)
    #         if (encodings := fr.face_encodings(image_num)) and len(encodings) == 1:
    #             encoding = encodings[0].tolist()
    #             new_face = Face(username=username, encoding=encoding)
    #             await new_face.insert()
    #             return True
    #         else:
    #             return False
    #
    async def recognize_face(image: UploadFile):
        img = await image.read()
        with open("./" + image.filename, mode="wb") as file:
            file.write(img)
        image_num = fr.load_image_file("./" + image.filename)
        if (encoding := fr.face_encodings(image_num)) and len(encoding) == 1:
            encodings = await Face.find_all().to_list()
            for target in encodings:
                result = fr.compare_faces(
                    np.fromiter(target.encoding, np.float64), encoding
                )
                if result[0]:
                    return True
            return False
