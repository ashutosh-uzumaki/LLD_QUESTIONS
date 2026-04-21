GET    /api/v1/wallets/{walletId}/balance
       → 200: { balance: 500.00, currency: "INR" }

GET    /api/v1/wallets/{walletId}/transactions
       → 200: [ { txnId, amount, type, timestamp } ]

POST   /api/v1/wallets/{walletId}/deposit
       Body: { amount: 500.00, currency: "INR" }
       → 200: { newBalance: 1000.00 }

POST   /api/v1/wallets/{walletId}/withdraw
       Body: { amount: 200.00, currency: "INR" }
       → 200 or 402 Payment Required (insufficient funds)

POST   /api/v1/transactions/transfer
       Body: { fromWalletId, toWalletId, amount, currency }
       → 200 or 402
