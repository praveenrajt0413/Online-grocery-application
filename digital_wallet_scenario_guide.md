# Scenario Adaptation Guide: Digital Wallet Reward System

If you are given a new scenario in an exam (like a **Digital Wallet Reward System**), you don't need to panic or rewrite everything. The infrastructure we created (`pom.xml`, `Dockerfile`, `deployment.yaml`, `Jenkinsfile`) is completely reusable. You only need to focus on identifying the **Nouns** and **Verbs** of the new problem and mapping them into `App.java` and `AppTest.java`.

Here is a step-by-step cheat sheet on how to adapt your code for any new scenario.

---

## 1. Identify the Domain Models (The "Nouns")
Read the problem statement and look for the core entities. 

* **Grocery system examples:** `Order`, `Item`, `DeliverySlot`
* **Digital Wallet examples:** `UserWallet`, `Transaction`, `RewardTier`

**What to do:**
Replace the static classes inside `App.java` with your new models.
```java
public static class UserWallet {
    private String userId;
    private double balance;
    private int rewardPoints;
    // constructors, getters, setters
}

public static class Transaction {
    private double amount;
    private String type; // e.g., "PURCHASE", "DEPOSIT"
    // constructors, getters, setters
}
```

---

## 2. Identify the Business Logic & Rules (The "Verbs" and "Conditions")
Next, figure out what the system needs to *do* and what *rules* it must follow.

* **Grocery system rules:** "Order must be > $10", "Slot cannot be in the past".
* **Digital Wallet rules:** "Users get 1 point for every $10 spent", "Cannot redeem points if balance is < 100".

**What to do:**
Replace the methods inside `App.java` (like `validateOrder`) with your new core logic methods.
```java
public int calculateRewardPoints(Transaction transaction) {
    if (transaction == null || transaction.getAmount() <= 0) {
        return 0; // Invalid transaction gets no points
    }
    // Rule: 1 point for every $10 spent
    return (int) (transaction.getAmount() / 10); 
}

public boolean redeemPoints(UserWallet wallet, int pointsToRedeem) {
    if (pointsToRedeem < 100) {
         System.out.println("Minimum 100 points required to redeem.");
         return false; // Fails minimum rule
    }
    // Process redemption...
    return true;
}
```

---

## 3. Write Tests (The Verification)
For every rule you identified in Step 2, you need a test in `AppTest.java`. Think about the **Happy Path** (everything works) and the **Edge Cases** (things fail).

* **Grocery tests:** Tested a $3 order (fails) and a $15 order (passes).
* **Digital Wallet tests:** Test a $5 transaction (0 points), a $25 transaction (2 points), and redeeming 50 points (fails).

**What to do:**
Replace the `@Test` methods in `AppTest.java` with new ones.
```java
@Test
public void testCalculateRewardsValidTransaction() {
    App.Transaction tx = new App.Transaction(25.0, "PURCHASE");
    int points = app.calculateRewardPoints(tx);
    assertEquals(2, points); // 25 / 10 = 2
}

@Test
public void testRedeemPointsBelowMinimum() {
    App.UserWallet wallet = new App.UserWallet("U1", 50.0, 150);
    assertFalse(app.redeemPoints(wallet, 50)); // Trying to redeem 50, minimum is 100!
}
```

---

## 4. What stays exactly the same? (The Infrastructure)

You do **NOT** need to change the following files unless the scenario specifically asks you to (e.g., "Change the port to 9090"):

- `pom.xml`: It will still find `App.java` and `AppTest.java` and compile them perfectly.
- `Dockerfile`: It still builds the jar and runs the `App` class.
- `Jenkinsfile`: The CI/CD pipeline steps (Checkout, Build, Docker Build) remain completely identical.
- `deployment.yaml`: Kubernetes doesn't care if the app inside the container is calculating groceries or wallet rewards, it just runs the container. 

### Summary for Exams:
1. **Wipe out** the old Nouns/Verbs in `App.java`.
2. **Write in** the new Nouns/Verbs.
3. **Wipe out** the old tests in `AppTest.java`.
4. **Write in** tests that prove your new Verbs work.
5. **Run `mvn clean test`**. If it passes, you are done!
