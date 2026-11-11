# CS 2114 Project 3 - CoffeeQueue

Coffee order queue for Deliverable 3, First Prototype

## What the prototype does

- Places an order using a customer name and menu items
- Keeps waiting orders in a custom singly linked FIFO queue
- Completes the first order, cancels a customer’s first waiting order, searches by ID, and shows queue position
- Keeps completed and cancelled orders searchable
- Uses a small console interface that goes through `LineController` instead of calling the model directly
- Handles bad menu choices, unknown items, invalid order IDs, empty queues, and blank customer names without crashing

## How to run

Use Java 17 or newer and run these commands from the repository root

```bash
mkdir -p out
javac -d out src/coffeeshop/*.java
java -cp out coffeeshop.CoffeeQueueDriver
```

This driver exercises the model **without any view or controller**, which is required for Deliverable 3

For the interactive console view:

```bash
java -cp out coffeeshop.Main
```

Try `1` to place an order, `7` to see the queue, `2` to complete the next order, `3` to cancel, `4` to check a position, and `5` to search by ID

For JUnit 5 tests, import the repository as a Maven project in Eclipse, or use Maven if installed:

```bash
mvn test
```

`pom.xml` uses the existing `src` and `test` directories as source roots and JUnit Jupiter 5.10.2 for testing

## Verification notes

- Java source compiled successfully with `javac` in the preparation environment
- The model-only `CoffeeQueueDriver` and interactive `Main` both ran successfully
- All 19 test methods were exercised with a temporary JUnit-compatible assertion harness and passed locally

## Reflection

### What changed from the specification, and why

| Spec said | What is in this prototype | What prompted the change |
|---|---|---|
| Java model classes and a singly linked `CoffeeQueue` with `head`, `tail`, and `size` | Kept the existing queue and its core FIFO, cancel, search, and position algorithms | The initial code already implemented the main model logic, so we kept it instead of rewriting it |
| Views `CusView` and `BarView` get Strings from `LineController` | Added both view classes and `LineController`, and kept the teammate’s `SimpleView` as a small console input/output view | The original prototype had a driver and a print-only view, but no controller-based interactive MVP path |
| Model constructors reject invalid names and prices | Added validation to `Customer` and `MenuItem`, plus `Customer.hashCode()` | The first prototype allowed blank names and negative prices, and `equals()` needed a matching `hashCode()` |
| Orders are in exactly one state, WAITING, COMPLETED, or CANCELLED | Kept `Order` and its status field but restricted status changes to the queue, and stopped duplicate enqueueing | The model invariants could otherwise be broken by putting the same order in the queue twice or reusing a finished order |
| `Order.describe()` shows the total | Kept the original description and formatted money with two decimal places | The original output could display inconsistent decimal formatting |
| JUnit tests for model methods and edge cases | Added JUnit tests for the queue, order, customer, menu item, controller, and console view | Deliverable 3 Exceeds asks for passing core queue tests and bad-input handling |
| Source classes belong to the same Java package | Renamed extensionless files to `.java`, moved them under `src/coffeeshop`, and fixed driver/view package declarations | The uploaded prototype had Java files at the repository root with inconsistent package declarations |

The main public queue method signatures from Section 5 are unchanged, including `enqueue`, `dequeue`, `peek`, `isEmpty`, `size`, `cancelOrder`, `findPosition`, and `searchOrder`

One extra implementation detail is that `Order.setStatus` is now package-private instead of public, so the queue controls when an order finishes or gets cancelled

### Did the model stand alone

Yes, the `CoffeeQueueDriver` only creates model objects and runs queue operations, and it does not create a view or controller

The model has no imports from `CusView`, `BarView`, or `SimpleView`, and the console view communicates with the model through `LineController`

### What happened to our unknowns

From the prototype work, we now have working examples of linked-node enqueue/dequeue, removing a node from the middle of the queue, resetting the tail after the last removal, and passing Strings between the controller and views

**Complete:** Compare this with our scope document.

### What does not work yet

This is still a console prototype, not a polished GUI

The two role-specific views currently display controller-generated text while `SimpleView` collects console input, so we can improve the separation of customer and barista actions in Deliverable 4

## Next Steps

### Finish the MVP in this order

1. **Everyone** — import the project and run the model driver, console demo, and all JUnit tests, then fix any integration failures
2. **Luke + Charlotte (proposed)** — review the model and tests together, especially cancellation, null inputs, and queue invariants
3. **Lucas (proposed)** — confirm controller methods match Section 5 and that invalid input never partially adds an order
4. **Kim (proposed)** — improve the customer and barista console prompts without making either view depend on model classes
5. **Everyone** — review the README, agree on the Deliverable 4 feature, and fill in actual file attribution before submission

### Non-trivial change feature for Deliverable 4

**Proposed feature: estimated wait time per order**

This adds queue-based logic instead of only changing a label - the model would calculate how long the orders ahead of a customer may take, using a simple per-item preparation estimate

**Prediction with specific classes and methods:**

- `CoffeeQueue` (model) — add `estimateWaitMinutes(int orderId)` to walk the waiting nodes ahead of the matching order and calculate the estimate, returning `-1` for unknown or no-longer-waiting orders
- `LineController` (controller) — add `estimateWaitMinutes(int orderId)` to return the model result to the view without exposing Nodes
- `SimpleView` (view) — add a console choice to request and display the estimate
- `CusView` (view) — update its customer-facing text to show the estimate when a customer checks an order
- `Order`, `Customer`, `MenuItem`, and `BarView` — **should not need changes**, since they do not own queue position or estimate logic

This feature is **planned, not implemented in Deliverable 3**, so we can compare this prediction with the actual changes in Deliverable 4

### Stretch goals

**Proposed commitment:** keep search by order ID, which is already part of our current MVP and working model

**Proposed drop for this checkpoint:** drink pictures, GUI, rewards/discounts, and personalized recommendations, so the queue and validation are reliable first

**Team check:** Confirm which stretch goals everyone actually wants to commit to for Deliverable 4 before submitting

### New tests for Deliverable 4

- `CoffeeQueueTest` — wait time for the first, middle, and last waiting order, and after an order is completed or cancelled
- `CoffeeQueueTest` — unknown ID, empty queue, and multiple items in earlier orders
- `LineControllerTest` — estimate returned for a valid order and safe result for an invalid ID
- `SimpleViewTest` — new wait-time console choice, including non-numeric input

### Who does what

| Remaining item | Proposed owner |
|---|---|
| Queue model and queue tests | Kim + Charlotte |
| Customer and barista views and console input | Luke |
| Controller and integration | Lucas |
| Demo, README, feature choice, and review | Everyone |

## Attribution

| Class or test file | Contribution so far | Team member(s) to confirm |
|---|---|---|
| `CoffeeQueue.java` | Original teammate implementation | **Confirmed by Charlotte** |
| `CoffeeQueueDriver.java` | Original teammate model driver preserved; package/comment cleanup | **Confirmed by Charlotte** |
| `Customer.java` | Original teammate class; GenAI-assisted validation and `hashCode` addition | **Confirmed by Charlotte** |
| `MenuItem.java` | Original teammate class | **Confirmed by Charlotte** |
| `Order.java` | Original teammate class | **Confirmed by Charlotte** |
| `SimpleView.java` | Original print-only class; GenAI-assisted console input implementation | **Confirm original author and reviewer** |
| `CusView.java` | GenAI-generated new view, needs teammate review | **Confirmed By Kim** |
| `BarView.java` | GenAI-generated new view, needs teammate review | **Confirmed By Kim** |
| `LineController.java` | GenAI-generated controller, needs teammate review | **Confirmed By Kim** |
| `Main.java` | GenAI-generated console launcher, needs teammate review | **Confirmed By Kim** |
| `CoffeeQueueTest.java` | Wrote, revised, reviewed, tested, GenAI-assisted | **Confirmed By Kim** |
| `CustomerTest.java` | Wrote, revised, reviewed, tested | **Confirmed By Kim** |
| `MenuItemTest.java` | Wrote, revised, reviewed, tested | **Confirmed By Kim** |
| `OrderTest.java` | Wrote, revised, reviewed, tested, GenAI-assisted | **Confirmed By Kim** |
| `LineControllerTest.java` | Wrote, revised, reviewed, tested, GenAI-assisted | **Confirmed By Kim** |
| `SimpleViewTest.java` | Wrote, revised, reviewed, tested | **Confirmed By Kim** |
| `ViewTest.java` | Wrote, revised, reviewed, tested | Kim | **Confirmed By Kim** |

### GenAI note

We used GenAI to organize our files and classes and to verify that our work aligned well against the rubric and requirements. It also helped facilitate the testing process by drafting initial structures, but we implemented the edge cases and handled them ourselves. It also helped us a README file so that we can fill it out with the respective changes and informaiton. As a team, we reviewed and tested all the generated code, kept our original queue implementation and driver as the starting point, and made targeted changes to guard against invalid state.

**Team check:** Review and test all code, document any additional AI tools used.
