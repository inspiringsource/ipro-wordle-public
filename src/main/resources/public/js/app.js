const ROWS = 6;
const COLS = 5;

const boardEl = document.getElementById("board");
const formEl = document.getElementById("guessForm");
const inputEl = document.getElementById("guess");
const resultEl = document.getElementById("result");
const submitBtn = document.getElementById("submitBtn");

let attempt = 0;

// Build 6x5 grid on load
const cells = [];
for (let i = 0; i < ROWS * COLS; i++) {
  const cell = document.createElement("div");
  cell.className = "cell";
  cell.textContent = "";
  boardEl.appendChild(cell);
  cells.push(cell);
}

function setStatus(msg, kind) {
  resultEl.className = kind || "";
  resultEl.textContent = msg;
}

function fillRow(rowIndex, word, feedback) {
  const start = rowIndex * COLS;

  for (let i = 0; i < COLS; i++) {
    const cell = cells[start + i];
    const ch = word[i] || "";
    const f = feedback?.[i] || "B";

    cell.textContent = ch;

    // reset state classes
    cell.classList.remove("state-g", "state-y", "state-b");

    if (f === "G") cell.classList.add("state-g");
    else if (f === "Y") cell.classList.add("state-y");
    else cell.classList.add("state-b");
  }
}

function lockGame() {
  inputEl.disabled = true;
  submitBtn.disabled = true;
}

formEl.addEventListener("submit", async (e) => {
  e.preventDefault();

  if (attempt >= ROWS) {
    setStatus("No attempts left.", "error");
    lockGame();
    return;
  }

  let guess = (inputEl.value || "").trim().toUpperCase();

  // Basic validation (exactly 5 letters)
  if (!/^[A-ZÄÖÜ]{5}$/.test(guess)) {
    setStatus("Please enter exactly 5 letters.", "error");
    return;
  }

  // Helper: safely parse JSON or throw a readable error
  async function readJsonOrThrow(res) {
    const ct = res.headers.get("content-type") || "";
    if (ct.includes("application/json")) {
      return await res.json();
    }
    const txt = await res.text();
    throw new Error(txt || `HTTP ${res.status}`);
  }

  try {
    // Try current endpoint first
    let res = await fetch("/guess", {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        Accept: "application/json",
      },
      body: new URLSearchParams({ guess }),
    });

    // If backend still uses the old route, retry legacy endpoint
    if (res.status === 404) {
      res = await fetch("/postTest", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
          Accept: "application/json",
        },
        body: new URLSearchParams({ postTest: guess }),
      });
    }

    if (!res.ok) {
      const txt = await res.text();
      throw new Error(txt || `HTTP ${res.status}`);
    }

    // We expect JSON: { word: "ABCDE", feedback: "GYBBY" }
    const data = await readJsonOrThrow(res);
    const word = (data && data.word ? String(data.word) : "").toUpperCase();
    const feedback = (
      data && data.feedback ? String(data.feedback) : "BBBBB"
    ).toUpperCase();

    fillRow(attempt, word, feedback);
    attempt += 1;

    inputEl.value = "";
    inputEl.focus();

    if (attempt >= ROWS) {
      setStatus("Done. You used all 6 attempts.", "ok");
      lockGame();
    } else {
      setStatus(`Attempt ${attempt} of ${ROWS} recorded.`, "ok");
    }
  } catch (err) {
    setStatus("Failed to submit. " + (err?.message || ""), "error");
  }
});

// Convenience: keep input uppercased
inputEl.addEventListener("input", () => {
  inputEl.value = inputEl.value.toUpperCase();
});

inputEl.focus();
