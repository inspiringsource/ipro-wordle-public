const ROWS = 6;
const COLS = 5;

const boardEl = document.getElementById("board");
const formEl = document.getElementById("guessForm");
const inputEl = document.getElementById("guess");
inputEl.setAttribute("readonly", "true");
const resultEl = document.getElementById("result");
const submitBtn = document.getElementById("submitBtn");
const restartBtn = document.getElementById("restartBtn");
const winModal = document.getElementById("winModal");
const playAgainBtn = document.getElementById("playAgainBtn");
const loseModal = document.getElementById("loseModal");
const tryAgainBtn = document.getElementById("tryAgainBtn");
const confettiContainer = document.getElementById("confetti");
const kbToggleBtn = document.getElementById("kbToggleBtn");
const autoSubmitBtn = document.getElementById("autoSubmitBtn");
const keyboardContainer = document.getElementById("keyboardContainer");

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

// Invalid Word modal elements
const invalidWordModal = document.getElementById("invalidWordModal");
const invalidWordOkBtn = document.getElementById("invalidWordOkBtn");

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


let currentGuess = "";
let autoSubmitEnabled = localStorage.getItem("autoSubmitEnabled") === "true";
let submitInProgress = false;
let lastAutoSubmittedAttempt = -1;
let keyboard = null;

function clampGuess(s) {
  // keep only letters A-Z and German umlauts, max 5
  const cleaned = (s || "")
    .toUpperCase()
    .replace(/[^A-ZÄÖÜ]/g, "")
    .slice(0, COLS);
  return cleaned;
}

function renderCurrentRow() {
  const rowStart = attempt * COLS;
  for (let i = 0; i < COLS; i++) {
    const ch = currentGuess[i] || "";
    cells[rowStart + i].textContent = ch;
  }
  // keep the existing form submission compatible
  inputEl.value = currentGuess;
}

function resetCurrentGuess() {
  currentGuess = "";
  inputEl.value = "";
  if (keyboard) keyboard.setInput("");
  renderCurrentRow();
}

function setGuessFromAnyInput(next) {
  currentGuess = clampGuess(next);
  currentGuess = clampGuess(next);
  inputEl.value = currentGuess;
  renderCurrentRow();
  if (keyboard) keyboard.setInput(currentGuess);
  maybeAutoSubmit();
}

function appendChar(ch) {
  if (currentGuess.length >= COLS) return;
  setGuessFromAnyInput(currentGuess + ch);
}

function backspaceChar() {
  setGuessFromAnyInput(currentGuess.slice(0, -1));
}

function maybeAutoSubmit() {
  if (!autoSubmitEnabled) return;
  if (currentGuess.length !== COLS) return;
  if (submitInProgress) return;
  // Prevent infinite loops if word is invalid or already tried for this row
  if (lastAutoSubmittedAttempt === attempt) return;

  lastAutoSubmittedAttempt = attempt;
  submitGuess();
}

function submitGuess() {
  // trigger your existing submit handler
  formEl.requestSubmit();
}

document.addEventListener("keydown", (e) => {
  if (keyboardContainer && keyboardContainer.hidden === false) {
    // allow typing even when virtual keyboard is open
  }
  if (e.key === "Enter") {
    e.preventDefault();
    submitGuess();
    return;
  }
  if (e.key === "Backspace") {
    e.preventDefault();
    backspaceChar();
    return;
  }
  // Accept letters and German umlauts
  const key = e.key;
  if (/^[a-zA-ZäöüÄÖÜ]$/.test(key)) {
    e.preventDefault();
    appendChar(key.toUpperCase());
  }
});




let isVirtualKeyboardMode = false;

function initKeyboardIfNeeded() {
  if (keyboard) return;

  // simple-keyboard UMD exposes window.SimpleKeyboard.default
  keyboard = new window.SimpleKeyboard.default({
    layoutName: "default",
    layout: {
      default: [
        "1 2 3 4 5 6 7 8 9 0",
        "Q W E R T Z U I O P Ü",
        "A S D F G H J K L Ö Ä",
        "Y X C V B N M",
        "{space} {bksp}"
      ]
    },
    display: {
      "{bksp}": "⌫",
      "{enter}": "Enter",
      "{space}": "Space"
    },
    onChange: (input) => {
      // Called when keyboard input changes (e.g. by clicking letter keys)
      setGuessFromAnyInput(input);
    },
    onKeyPress: (button) => {
      if (button === "{enter}") submitGuess();
      if (button === "{bksp}") backspaceChar();
    }
  });

  keyboard.setInput(currentGuess);
}

function updateInputModeUI() {
  if (isVirtualKeyboardMode) {
    // Virtual Mode
    keyboardContainer.hidden = false;
    inputEl.hidden = true;
    kbToggleBtn.textContent = "Keyboard: On";
    initKeyboardIfNeeded();
  } else {
    // Physical Mode
    keyboardContainer.hidden = true;
    inputEl.hidden = false;
    kbToggleBtn.textContent = "Keyboard: Off";
    // We do NOT explicitly focus inputEl here to avoid mobile keyboard popups,
    // as per user request. "Wordle-style" means we just listen to global keydown.
  }
}

kbToggleBtn.addEventListener("click", () => {
  isVirtualKeyboardMode = !isVirtualKeyboardMode;
  updateInputModeUI();
});

// Auto-submit toggle
function updateAutoSubmitUI() {
  if (autoSubmitEnabled) {
    autoSubmitBtn.textContent = "Auto: On";
    // Hide submit button in auto mode
    submitBtn.hidden = true;
  } else {
    autoSubmitBtn.textContent = "Auto: Off";
    submitBtn.hidden = false;
  }
}

autoSubmitBtn.addEventListener("click", () => {
  autoSubmitEnabled = !autoSubmitEnabled;
  localStorage.setItem("autoSubmitEnabled", autoSubmitEnabled);
  updateAutoSubmitUI();
  // If we just enabled it and have a full row, try submitting
  maybeAutoSubmit();
});

updateAutoSubmitUI();

// Initialize UI
updateInputModeUI();

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

  if (submitInProgress) return;
  submitInProgress = true;

  if (attempt >= ROWS) {
    setStatus("No attempts left.", "error");
    lockGame();
    submitInProgress = false;
    return;
  }

  let guess = (currentGuess || inputEl.value || "").trim().toUpperCase();

  // Basic validation (exactly 5 letters)
  if (!/^[A-ZÄÖÜ]{5}$/.test(guess)) {
    setStatus("Please enter exactly 5 letters.", "error");
    submitInProgress = false;
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

    // Helper: check if message indicates an invalid word
    function isInvalidWordMessage(msg) {
      if (!msg) return false;
      const lower = msg.toLowerCase();
      return (
        lower.includes("kein gueltiges deutsches wort") ||
        lower.includes("kein gültiges deutsches wort") ||
        lower.includes("not a valid german word") ||
        lower.includes("invalid word")
      );
    }

    if (!res.ok) {
      const ct = res.headers.get("content-type") || "";
      let errorMsg = `HTTP ${res.status}`;

      if (ct.includes("application/json")) {
        const errData = await res.json();
        errorMsg = errData?.error || errData?.message || errorMsg;
      } else {
        errorMsg = (await res.text()) || errorMsg;
      }

      // Check for invalid word response
      if (isInvalidWordMessage(errorMsg)) {
        showInvalidWordModal();
        return;
      }
      throw new Error(errorMsg);
    }

    // We expect JSON: { word: "ABCDE", feedback: "GYBBY" }
    const data = await readJsonOrThrow(res);

    // Also check if a 200 response contains an error/message field indicating invalid word
    if (data && (data.error || data.message)) {
      const msg = data.error || data.message;
      if (isInvalidWordMessage(msg)) {
        showInvalidWordModal();
        return;
      }
    }
    const word = (data && data.word ? String(data.word) : "").toUpperCase();
    const feedback = (
      data && data.feedback ? String(data.feedback) : "BBBBB"
    ).toUpperCase();

    fillRow(attempt, word, feedback);
    attempt += 1;

    resetCurrentGuess();
    inputEl.blur();

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
  } finally {
    submitInProgress = false;
  }
});

// Convenience: keep input uppercased
inputEl.addEventListener("input", () => {
  inputEl.value = inputEl.value.toUpperCase();
});

// Restart (Week 2): request new word from server, then reload UI
restartBtn?.addEventListener("click", async () => {
  resetCurrentGuess();
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
  resetCurrentGuess();
  try {
    await fetch("/new-game", { method: "POST" });
    location.reload();
  } catch (err) {
    setStatus("Fehler beim Neustarten.", "error");
  }
});

// Try again button (lose modal)
tryAgainBtn?.addEventListener("click", async () => {
  resetCurrentGuess();
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
  resetCurrentGuess();
  try {
    await fetch("/new-game", { method: "POST" });
    location.reload();
  } catch (err) {
    setStatus("Fehler beim Neustarten.", "error");
  }
});

// Invalid Word modal functions
function showInvalidWordModal() {
  invalidWordModal.hidden = false;
}

function hideInvalidWordModal() {
  invalidWordModal.hidden = true;
  inputEl.value = "";
  inputEl.focus();
}

invalidWordOkBtn?.addEventListener("click", hideInvalidWordModal);

inputEl.focus();
