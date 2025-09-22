package it.uniupo.studenti.mobishare.backend_core.exception

class EmailAlreadyExistsException(email: String) : RuntimeException(
    "Email '${email}' already exists."
)
