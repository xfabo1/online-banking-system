from locust import HttpUser, task, between
import json

class ObsUser(HttpUser):

    host = "http://localhost:8082"

    @task
    def test_task(self):
        datajson = {
            "withdrawsFromAccountNumber": "bank-1",
            "depositsToAccountNumber": "account-1",
            "withdrawAmount": 1000
        }

        request = json.dumps(datajson)

        response = self.client.post("/api/transaction-service/v1/transactions/transaction/create", data=request, headers={'Content-Type': 'application/json', 'Accept': 'application/json'})

        print(response.json())