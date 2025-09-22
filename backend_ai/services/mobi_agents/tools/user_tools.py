import os
from datetime import datetime

from agno.tools import Toolkit
from dotenv import load_dotenv
from fastapi import HTTPException
from httpx import AsyncClient
from pydantic import BaseModel, ValidationError

load_dotenv()
BACKEND_CORE = os.getenv("BACKEND_CORE")


class HowToResponse(BaseModel):
	guide: str


class Transaction(BaseModel):
	id: int
	value: float
	time: datetime


class TransactionList(BaseModel):
	transactions: list[Transaction]


class BalanceResponse(BaseModel):
	credit: float
	greenpoints: int


class UserTools(Toolkit):
	def __init__(self, token: str, username: str, **kwargs):
		super().__init__(
			name="user_tools",
			tools=[
				self.how_to_start_race,
				self.how_to_wallet,
				self.how_to_personal_information,
				self.action_get_payments_history,
				self.action_recharge_custom_amount,
                self.action_get_current_balance,
			],
			**kwargs,
		)
		self.token = token
		self.username = username

	def how_to_start_race(self) -> HowToResponse:
		"""
		Guida l'utente passo dopo passo per iniziare una corsa con un veicolo specifico.
		Legge la guida dalle risorse del progetto e restituisce un testo semplice.
		"""
		with open("resources/user_guide/start_race.txt", "r") as file:
			guide = file.read()
			return HowToResponse(guide=guide)

	def how_to_wallet(self) -> HowToResponse:
		"""
		Guida l'utente sulla gestione del portafoglio dell'applicazione.
		Legge la guida dalle risorse del progetto e restituisce un testo semplice.
		fornisce informazioni su come aggiungere crediti e vedere il saldo attuale.
		fornisce informazioni su come visualizzare la cronologia delle transazioni.
		"""
		with open("resources/user_guide/wallet.txt", "r") as file:
			guide = file.read()
			return HowToResponse(guide=guide)

	def how_to_personal_information(self) -> HowToResponse:
		"""
		Guida l'utente sulla visualizzazione delle proprie informazioni personali
		Legge la guida dalle risorse del progetto e restituisce un testo semplice.
		Fornisce informazioni su come visualizzare le proprie anagrafiche.
		fornisce anche informazioni su come effettuare il logout (uscire) dal proprio account.
		"""
		with open("resources/user_guide/profile.txt", "r") as file:
			guide = file.read()
			return HowToResponse(guide=guide)

	async def action_get_payments_history(self) -> TransactionList:
		"""
		fornisce la lista delle transazioni eseguite dall'utente dal momento dell'iscrizione.
		contiene sia gli addebiti che gli accrediti
		le transazioni positive sono accrediti, quelle negative sono pagamenti per l'utilizzo dei veicoli
		:return: TransactionList contenente la lista di transazioni validate
		:raises: HTTPException in caso di errori di comunicazione o validazione
		"""
		if not BACKEND_CORE:
			raise HTTPException(status_code=500, detail="BACKEND_CORE not configured")

		async with AsyncClient() as client:
			result = await client.get(
				f"http://{BACKEND_CORE}/customers/{self.username}/payments",
				headers={"Authorization": self.token},
			)

		if result.status_code != 200:
			raise HTTPException(status_code=result.status_code, detail=result.text)

		try:
			ret_data = result.json()
		except Exception:
			raise HTTPException(status_code=500, detail="Invalid JSON response")

		if not isinstance(ret_data, list):
			raise HTTPException(
				status_code=500,
				detail="Expected JSON array of transactions"
			)

		# Valida la struttura di ogni transazione usando Pydantic
		try:
			return TransactionList(transactions=ret_data)
		except ValidationError as e:
			raise HTTPException(
				status_code=500,
				detail=f"Invalid transaction data format: {e}"
			)

	async def action_recharge_custom_amount(self, amount: float = -1.0) -> None:
		"""
		permette di aggiungere credito al proprio portafoglio wallet
		:param amount: (opzionale) importo da aggiungere (float), ultimo effettuato se non presente
		:return: None
		:raises: HTTPException in caso di errori di comunicazione o validazione
		"""

		if amount < -1.0 or -1.0 < amount < 0.0:
			raise HTTPException(status_code=400, detail="Amount must be a positive float")

		if amount == -1.0:
			async with AsyncClient() as client:
				last_result = await client.post(
					f"http://{BACKEND_CORE}/customers/{self.username}/transactions/last",
					headers={"Authorization": self.token},
				)
			if last_result.status_code != 200:
				raise HTTPException(status_code=400,
								detail="Non esistono transazioni precedenti da replicare, fornire un importo valido")
			else:
				amount = last_result.json().get("value", 0.1)

		if not BACKEND_CORE:
			raise HTTPException(status_code=500, detail="BACKEND_CORE not configured")

		async with AsyncClient() as client:
			result = await client.post(
				f"http://{BACKEND_CORE}/customers/{self.username}/wallet/recharge?euro={amount}",
				headers={"Authorization": self.token},
			)

		if result.status_code != 200:
			raise HTTPException(status_code=result.status_code, detail=result.text)

		# Valida la struttura di ogni transazione usando Pydantic
		try:
			return None
		except ValidationError as e:
			raise HTTPException(
				status_code=500,
				detail=f"Impossibile ricaricare credito: {e}"
			)


	async def action_get_current_balance(self) -> BalanceResponse:
		"""
		fornisce credito e greenpoints attualmente nel portafogli dell'utente
		:return: BalanceResponse contenente il credito e i greenpoints attuali
		:raises: HTTPException in caso di errori di comunicazione o validazione
		"""
		if not BACKEND_CORE:
			raise HTTPException(status_code=500, detail="BACKEND_CORE not configured")

		async with AsyncClient() as client:
			result_credit = await client.get(
				f"http://{BACKEND_CORE}/customers/{self.username}/credit",
				headers={"Authorization": self.token},
			)
			print(self.token)
			result_greenpoints = await client.get(
				f"http://{BACKEND_CORE}/customers/{self.username}/greenPoints",
				headers={"Authorization": self.token},
			)

		if result_credit.status_code != 200:
			raise HTTPException(status_code=result_credit.status_code, detail=result_credit.text)
		if result_greenpoints.status_code != 200:
			raise HTTPException(status_code=result_greenpoints.status_code, detail=result_greenpoints.text)

		try:
			ret_data = (float(result_credit.text), int(result_greenpoints.text))
		except Exception:
			raise HTTPException(status_code=500, detail="Invalid response")

		# Valida la struttura di ogni transazione usando Pydantic
		try:
			return BalanceResponse(credit=ret_data[0], greenpoints=ret_data[1])
		except ValidationError as e:
			raise HTTPException(
				status_code=500,
				detail=f"Invalid transaction data format: {e}"
			)
