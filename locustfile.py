from locust import HttpUser, task, between
import json
import random

class ObsUser(HttpUser):
    host = "http://localhost:8082"
    accounts = [
        {"id": "5217d3f7-e716-48ac-8a45-e0c54257d203", "customerId": "4121add0-f5d7-4128-9c8f-e81fa93237c5", "currencyCode": "CZK"},
        {"id": "2aa6cf23-f3e1-49f0-9c2e-9032400becfa", "customerId": "4121add0-f5d7-4128-9c8f-e81fa93237c6", "currencyCode": "EUR"},
        {"id": "20451388-9dc6-44d6-b05d-c611071b6862", "customerId": "4121add0-f5d7-4128-9c8f-e81fa93237c7", "currencyCode": "EUR"},
        {"id": "086aa375-8386-4316-adb3-c10da97038ef", "customerId": "4121add0-f5d7-4128-9c8f-e81fa93237c7", "currencyCode": "CZK"},
        {"id": "90b3d598-b611-486e-845c-f2234beb1ce2", "customerId": "4121add0-f5d7-4128-9c8f-e81fa93237c8", "currencyCode": "EUR"},
        {"id": "c7148e7e-8b08-4323-a5fe-2be9b5f32514", "customerId": "4121add0-f5d7-4128-9c8f-e81fa93237c8", "currencyCode": "EUR", "isBankAccount": True}
    ]

    @task
    def test_task(self):
        datajson = {
            "withdrawsFromAccount": "c7148e7e-8b08-4323-a5fe-2be9b5f32514",
            "depositsToAccount": "90b3d598-b611-486e-845c-f2234beb1ce2",
            "withdrawAmount": 1000
        }

        request = json.dumps(datajson)

        response = self.client.post("/api/transaction-service/v1/transactions/transaction/create", data=request, headers={'Content-Type': 'application/json', 'Accept': 'application/json'})

        print(response.json())

    @task(3)  # Adjust the weight to define how frequently this task should be executed
    def transfer_task(self):
        sender_account = random.choice([acc for acc in self.accounts if not acc.get("isBankAccount")])
        receiver_account = random.choice([acc for acc in self.accounts if acc.get("id") != sender_account["id"]])

        amount = random.randint(10, 1000)  # You can adjust the range as per your requirements

        datajson = {
            "withdrawsFromAccount": sender_account["id"],
            "depositsToAccount": receiver_account["id"],
            "withdrawAmount": amount
        }

        request = json.dumps(datajson)

        response = self.client.post("/api/transaction-service/v1/transactions/transaction/create", data=request, headers={'Content-Type': 'application/json', 'Accept': 'application/json'})

        print(response.json())

    wait_time = between(0, 0.5)  # Adjust the wait time between tasks