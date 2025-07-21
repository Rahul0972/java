

# 🎲 Multiplayer Dice Game

An interactive Java Swing game for 2 to 5 players. Each player takes turns rolling a dice, and the player(s) with the highest roll wins! The game features a modern UI, custom backgrounds, and easy controls.

---

## ✨ Features
- Supports 2 to 5 players
- Enter custom player names
- Modern, colorful UI with background image
- Dice images and smooth roll animation
- Winner and draw detection
- Restart and exit buttons
- No external dependencies (pure Java/Swing)

## 📦 Project Structure
```
multiplayer-dice-game
├── src
│   ├── Main.java
│   ├── gui
│   │   └── GameWindow.java
│   ├── game
│   │   ├── Dice.java
│   │   ├── Player.java
│   │   └── GameLogic.java
│   └── utils
│       └── Constants.java
├── resources
│   ├── background.jpg/png
│   ├── dice1.png
│   ├── dice2.png
│   ├── dice3.png
│   ├── dice4.png
│   ├── dice5.png
│   └── dice6.png
├── README.md
```

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK 8 or higher)
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code, etc.) or terminal

### Setup & Run
1. **Clone this repository:**
   ```sh
   git clone https://github.com/yourusername/multiplayer-dice-game.git
   cd multiplayer-dice-game
   ```
2. **Ensure resources are available:**
   - Place `background.jpg` (or `background.png`) and `dice1.png` to `dice6.png` in the `resources` folder.
3. **Compile the code:**
   ```sh
   javac -cp src src/Main.java src/gui/GameWindow.java src/game/Dice.java src/game/Player.java src/game/GameLogic.java src/utils/Constants.java
   ```
4. **Run the game:**
   ```sh
   java -cp src:resources Main
   ```
   *(On Windows, use `;` instead of `:` for the classpath)*

## 🕹️ How to Play
- Enter the number of players (2-5) and their names.
- Each player clicks "Roll Dice" on their turn.
- The result and winner (or draw) are displayed at the end.
- Use "Restart" to play again or "Exit" to quit.

## 🖼️ Screenshots
<img width="753" height="546" alt="image" src="https://github.com/user-attachments/assets/07eca6fa-a9fd-4fe2-9311-8466484f69a4" />




## 📄 License
This project is open-source and available for modification and distribution. Enjoy the game!
