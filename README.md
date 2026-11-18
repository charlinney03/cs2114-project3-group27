# CS 2114 Project 3 — Campus Café Pickup
## Deliverable 4 — Second Prototype + Tests

Group 27 | Fall 2026 | Java 17 | JUnit 5

## What our prototype does

Our project manages a coffee-shop pickup line using a custom singly linked FIFO queue. Customers can place orders with one or more menu items, check their order position and status, request an estimated wait time, and cancel their oldest waiting order by name. The barista can complete the next order. Completed and cancelled orders remain searchable.

### New Deliverable 4 feature: estimated wait time

The model calculates a simple **3 minutes per menu item in orders ahead** of the requested waiting order. The first waiting order has a zero-minute estimate. Estimates update when orders are completed or cancelled. A nonexistent, completed, or cancelled order returns `-1` rather than crashing. This is a basic estimate, not a real-world guarantee or a clock-based timer.

- Model: `CoffeeQueue.estimateWaitMinutes(int orderId)` traverses waiting nodes
- Controller: `LineController.estimateWaitMinutes(int orderId)` forwards the result without exposing nodes
- View: console option **8** asks for an ID and prints the estimate or a safe error message

## How to compile and run

Requirements: **JDK 17 or newer**. Run commands from the repository root.

```bash
mkdir -p out
javac -d out src/coffeeshop/*.java
java -cp out coffeeshop.Main
```

The interactive menu supports:

| Option | Action |
|---|---|
| 1 | Place an order using a name and comma-separated menu items |
| 2 | Complete the next order |
| 3 | Cancel the oldest waiting order for a customer |
| 4 | Check position by order ID |
| 5 | Search by order ID (including finished orders) |
| 6 | Show menu |
| 7 | Show customer and barista line summaries |
| 8 | Estimate wait time by order ID |
| 0 | Exit |

Run the model-only driver without the view/controller:

```bash
java -cp out coffeeshop.CoffeeQueueDriver
```

### Run JUnit tests

Import the project as a Maven project in Eclipse, or run from the repository root:

```bash
mvn test
```

`pom.xml` configures JUnit Jupiter 5.10.2 and the existing `src` and `test` directories. **Before submission, run `mvn test` on your machine and confirm zero failures.** The generated archive was compiled and smoke-tested with `javac` and `java`; Maven/JUnit execution could not be verified in the creation environment.

## Design / MVC responsibilities

| Role | Classes | Responsibility |
|---|---|---|
| Model | `CoffeeQueue`, `Order`, `Customer`, `MenuItem` | Queue state, order status, validation, wait-time calculation |
| Controller | `LineController` | Validates input, invokes model operations, prepares text for views |
| View | `SimpleView`, `CusView`, `BarView` | Console interaction and customer/barista display text |
| Model-only demo | `CoffeeQueueDriver` | Demonstrates queue behavior without UI |
| Launcher | `Main` | Wires the queue, controller, views, and menu |

User input flows from `SimpleView` to `LineController`, which calls `CoffeeQueue`. The controller updates the role-specific views after successful queue changes. The views do not access linked nodes or calculate queue estimates.

## Bad input and test coverage

The queue rejects null or duplicate orders, null customers in cancellation, and null orders in position lookup. Empty `peek` and `dequeue` throw predictable `NoSuchElementException`s, and the controller checks emptiness before completing. Blank customer names, unknown menu items, missing menu items, nonnumeric console IDs, nonexistent order IDs, and early end-of-input are handled without crashing the interactive prototype.

Model tests cover FIFO order, interleaved enqueue/dequeue operations, head/middle/tail cancellation, search across statuses, empty queues, invalid inputs, and wait estimates before/after queue changes. Controller tests cover both valid and invalid operations; console tests cover interactive input. Tests must be rerun after merging any teammate changes.

## Reflection

### How did the design absorb the change?

Our D3 Next Steps predicted that estimated wait time would touch `CoffeeQueue`, `LineController`, `SimpleView`, and `CusView`, while `Order`, `Customer`, `MenuItem`, and `BarView` would stay unchanged. The feature implementation actually changed **`CoffeeQueue`, `LineController`, and `SimpleView`**. We also extended `CoffeeQueueTest`, `LineControllerTest`, and `SimpleViewTest` and updated the model-only driver to demonstrate the feature. We did **not** need to change `CusView`: `SimpleView` already prints controller-provided text, so the new console option was sufficient. That was the main difference from our prediction. The existing model classes we predicted would remain unchanged did not need changes for this feature.

### Did the change cross the model/view line?

The feature necessarily traveled through the three MVC layers, but the **calculation stayed in the model**. `CoffeeQueue` counts items ahead; `LineController` exposes an integer result; `SimpleView` formats it for the user. The view did not need to learn about queue nodes, traversal, or order storage. That separation made the change easier than putting queue logic in the UI.

### What changed from D3, and why?

We kept the proposed method name `estimateWaitMinutes(int orderId)` and the `-1` result for an unknown or no-longer-waiting order. We chose a simple, documented **3 minutes per item ahead** rule rather than adding per-drink timing fields. This keeps the model change focused and avoids changing `MenuItem` or `Order`. We added a new console choice, model/controller tests, and tests for nonnumeric and unknown IDs. We did not add a new customer-specific display method to `CusView` because the console already offers the feature directly.

### What does not work yet?

This is still a console prototype rather than a graphical application. The estimate does not account for concurrent baristas, drink complexity, or actual elapsed preparation time. Customer/barista summaries currently store the latest controller-supplied text, which is acceptable for the current console flow but could be improved so views never cache model-derived state. We must still run and verify the **actual Maven JUnit suite** in the team's Eclipse/Maven environment and correct any failures before lab. All team members should review the code and attribution entries.

### What design change would make the next feature cheaper?

A small read-only `QueueSnapshot` or display-data object produced by the controller could give both views a consistent representation of the current queue. That would reduce repeated text formatting and make future GUI changes easier without exposing linked-list internals. A preparation-time policy could also replace the fixed three-minute constant if we later support drink-specific estimates.

## Next Steps

### Remaining work, in priority order

1. **Everyone** — run `mvn test`, compile and run the interactive program, and confirm the new estimate works after both completion and cancellation
2. **Kim + Charlotte (proposed)** — review queue logic, estimate tests, and interleaved FIFO behavior
3. **Lucas (proposed)** — verify controller validation and ensure invalid inputs never partially add orders
4. **Luke (proposed)** — check customer/barista console flows and error messages
5. **Everyone** — review the final README, confirm attribution, rehearse the demo, and fix any issues found

### Stretch goals

**Committed:** search by order ID and estimated wait time. Search is already implemented and the estimate is the new D4 feature.

**Dropped for this checkpoint:** GUI, drink pictures, rewards/discounts, and personalized recommendations. We are prioritizing a stable queue, validation, and test coverage.

### Three-minute demo plan

| Time | What we will show |
|---|---|
| 0:00–0:25 | Introduce Campus Café Pickup and explain model/controller/view |
| 0:25–1:00 | Place Sam's two-drink order and Alex's one-drink order |
| 1:00–1:30 | Check Alex's position and **estimated wait (6 minutes)** |
| 1:30–1:55 | Complete Sam's order and check Alex's updated wait (**0 minutes**) |
| 1:55–2:20 | Enter an invalid order ID and nonnumeric ID to show safe handling |
| 2:20–2:45 | Explain the linked queue and show the JUnit test results |
| 2:45–3:00 | Summarize what changed from D3 and remaining improvements |

### Who does what?

The names below reflect **proposed** responsibilities from the earlier README.

| Item | Proposed owner |
|---|---|
| Queue and model tests | Kim + Charlotte |
| Customer/barista view and console checks | Luke |
| Controller and integration | Lucas |
| README, demo, and final verification | Everyone |

## Attribution

**Review this table before submission.** Attribution must describe each person's actual contribution; suggested responsibilities do not count as completed work. The original D3 README listed Charlotte as the original author of the queue and related model classes and Kim for the controller, view, and test scaffolding. Those prior attributions are carried forward as reported, not independently verified. Confirm the precise contributions with each teammate.

| Class or test file | Contribution / contributor to confirm |
|---|---|
| `CoffeeQueue.java` | Charlotte — original queue; Kim/GenAI-assisted D4 estimate implementation — confirm review and edits |
| `CoffeeQueueDriver.java` | Charlotte — original driver; D4 estimate demonstration updated with GenAI assistance — confirm reviewer |
| `Customer.java` | Charlotte — original; Kim/GenAI-assisted validation in D3 — confirm |
| `MenuItem.java` | Charlotte — original; D3 validation retained — confirm |
| `Order.java` | Charlotte — original; D3 validation retained — confirm |
| `SimpleView.java` | Wrote, revised, reviewed, tested, GenAI assisted |
| `CusView.java` | Kim - Wrote, revised, reviewed, tested |
| `BarView.java` | Kim - Wrote, revised, reviewed, tested |
| `LineController.java` | Kim - Wrote, revised, reviewed, tested, GenAI assisted using Deliverable 3 implementation|
| `Main.java` | Kim — GenAI-assisted D3 implementation; unchanged for D4 |
| `CoffeeQueueTest.java` | Kim - Wrote, revised, reviewed, tested  |
| `CustomerTest.java` | Kim - Wrote, revised, reviewed, tested  |
| `MenuItemTest.java` | Kim - Wrote, revised, reviewed, tested|
| `OrderTest.java` | Kim - Wrote, revised, reviewed, tested |
| `LineControllerTest.java` | Kim - Wrote, revised, reviewed, tested, GenAI assisted |
| `SimpleViewTest.java` | Kim - Wrote, revised, reviewed, tested, GenAI assisted, |
| `ViewTest.java` | Kim - Wrote, revised, reviewed, tested, GenAI assisted |

### GenAI note. 

We used GenAI to organize our files and classes and to verify that our work aligned well against the rubric and requirements. It also helped facilitate the testing process by drafting initial structures, but we implemented the edge cases and handled them ourselves. It also helped us a README file so that we can fill it out with the respective changes and informaiton. As a team, we reviewed and tested all the generated code, kept our original queue implementation and driver as the starting point, and made targeted changes to guard against invalid state.

