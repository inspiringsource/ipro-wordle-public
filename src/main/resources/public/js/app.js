const ROWS = 6;
const COLS = 5;

const boardEl = document.getElementById("board");
const formEl = document.getElementById("guessForm");
const inputEl = document.getElementById("guess");
const resultEl = document.getElementById("result");
const submitBtn = document.getElementById("submitBtn");
const restartBtn = document.getElementById("restartBtn");
const winModal = document.getElementById("winModal");
const playAgainBtn = document.getElementById("playAgainBtn");
const loseModal = document.getElementById("loseModal");
const tryAgainBtn = document.getElementById("tryAgainBtn");
const confettiContainer = document.getElementById("confetti");

// Reveal word elements
const revealBtn = document.getElementById("revealBtn");
const revealConfirmModal = document.getElementById("revealConfirmModal");
const revealSolutionModal = document.getElementById("revealSolutionModal");
const solutionText = document.getElementById("solutionText");
const newGameFromRevealBtn = document.getElementById("newGameFromRevealBtn");
const closeConfirmModal = document.getElementById("closeConfirmModal");
const revealCancelBtn = document.getElementById("revealCancelBtn");
const revealProceedBtn = document.getElementById("revealProceedBtn");
const loseSolutionText = document.getElementById("loseSolutionText");

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
      const ct = res.headers.get("content-type") || "";
      if (ct.includes("application/json")) {
        const errData = await res.json();
        const msg =
          errData && errData.error
            ? String(errData.error)
            : `HTTP ${res.status}`;
        throw new Error(msg);
      }
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

    // Check for win condition
    if (feedback === "GGGGG") {
      lockGame();
      showWinModal();
      return;
    }

    if (attempt >= ROWS) {
      setStatus("Fertig! Du hast alle 6 Versuche genutzt.", "ok");
      lockGame();
      showLoseModal();
    } else {
      setStatus(`Versuch ${attempt} von ${ROWS} aufgenommen.`, "ok");
    }
  } catch (err) {
    setStatus(err?.message || "Fehler beim Absenden.", "error");
  }
});

// Convenience: keep input uppercased
inputEl.addEventListener("input", () => {
  inputEl.value = inputEl.value.toUpperCase();
});

// Restart (Week 2): request new word from server, then reload UI
restartBtn?.addEventListener("click", async () => {
  try {
    await fetch("/new-game", { method: "POST" });
    location.reload();
  } catch (err) {
    setStatus("Fehler beim Neustarten.", "error");
  }
});

// Win modal functions
function showWinModal() {
  winModal.hidden = false;
  createConfetti();
}

async function showLoseModal() {
  // Fetch solution before showing lose modal
  try {
    const res = await fetch("/solution", {
      method: "GET",
      headers: { Accept: "application/json" },
    });
    if (res.ok) {
      const data = await res.json();
      const word = data.zielwort || "---";
      loseSolutionText.textContent = `Lösungswort war: ${word}`;
    } else {
      loseSolutionText.textContent = "Lösungswort konnte nicht geladen werden.";
    }
  } catch (err) {
    loseSolutionText.textContent = "Lösungswort konnte nicht geladen werden.";
  }
  loseModal.hidden = false;
}

function createConfetti() {
  const colors = [
    "#2e7d32",
    "#f9a825",
    "#1976d2",
    "#e91e63",
    "#9c27b0",
    "#ff5722",
  ];
  const pieceCount = 50;

  for (let i = 0; i < pieceCount; i++) {
    const piece = document.createElement("div");
    piece.className = "confetti-piece";
    piece.style.left = Math.random() * 100 + "%";
    piece.style.backgroundColor =
      colors[Math.floor(Math.random() * colors.length)];
    piece.style.animationDelay = Math.random() * 0.5 + "s";
    piece.style.animationDuration = 2 + Math.random() * 2 + "s";
    confettiContainer.appendChild(piece);
  }

  // Clean up confetti after animation
  setTimeout(() => {
    confettiContainer.innerHTML = "";
  }, 4000);
}

// Play again button
playAgainBtn?.addEventListener("click", async () => {
  try {
    await fetch("/new-game", { method: "POST" });
    location.reload();
  } catch (err) {
    setStatus("Fehler beim Neustarten.", "error");
  }
});

// Try again button (lose modal)
tryAgainBtn?.addEventListener("click", async () => {
  try {
    await fetch("/new-game", { method: "POST" });
    location.reload();
  } catch (err) {
    setStatus("Fehler beim Neustarten.", "error");
  }
});

// Reveal word button - opens confirm modal only (no fetch yet)
revealBtn?.addEventListener("click", () => {
  revealConfirmModal.hidden = false;
});

// Cancel reveal - close confirm modal, game continues
revealCancelBtn?.addEventListener("click", () => {
  revealConfirmModal.hidden = true;
});

// Close confirm modal (same as cancel)
closeConfirmModal?.addEventListener("click", () => {
  revealConfirmModal.hidden = true;
});

// Proceed with reveal - lock game, fetch solution, show solution modal
revealProceedBtn?.addEventListener("click", async () => {
  // Lock the game
  lockGame();
  revealBtn.disabled = true;

  // Fetch the solution
  try {
    const res = await fetch("/solution", {
      method: "GET",
      headers: { Accept: "application/json" },
    });
    if (res.ok) {
      const data = await res.json();
      const word = data.zielwort || "---";
      solutionText.textContent = word;
    } else {
      solutionText.textContent = "Lösungswort konnte nicht geladen werden.";
    }
  } catch (err) {
    solutionText.textContent = "Lösungswort konnte nicht geladen werden.";
  }

  // Close confirm modal and open solution modal
  revealConfirmModal.hidden = true;
  revealSolutionModal.hidden = false;
});

// New game from reveal solution modal
newGameFromRevealBtn?.addEventListener("click", async () => {
  try {
    await fetch("/new-game", { method: "POST" });
    location.reload();
  } catch (err) {
    setStatus("Fehler beim Neustarten.", "error");
  }
});

inputEl.focus();
