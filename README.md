# Creative Keys

A lightweight NeoForge mod that adds a Creative Key item for server recordings and content creation. Use it as a prop or utility tool in videos with a clean, professional timer display.

## Features

- **Creative Key Item**: A golden, enchanted key that grants 30 minutes of Creative Mode
- **HUD Timer Display**: Clean overlay showing remaining Creative time with key icon
- **Pause/Resume System**: Advanced timer controls for content creators
- **Admin Commands**: Full command suite for server management
- **Lightweight**: Minimal performance impact, fully compatible with other mods

## Usage

### Basic Usage
1. Right-click with a Creative Key to activate 30 minutes of Creative Mode
2. Using additional keys extends the timer (stackable duration)
3. Timer automatically reverts to Survival when expired
4. Real-time HUD display shows remaining time

### Timer Display
- **Active Timer**: Shows `M:SS` format with key icon (top-left)
- **Paused Timer**: Shows `M:SS (paused)` in gray when paused
- **Clean Interface**: Positioned above hotbar, non-intrusive design

## Commands

All commands require OP level 2 (admin permissions):

```
/creativekeys give <targets>              # Give Creative Key to players
/creativekeys pause <targets>             # Pause active timer
/creativekeys resume <targets>            # Resume paused timer  
/creativekeys reset <targets>             # Clear timer completely
/creativekeys add <targets> <seconds>     # Add time to active/paused timer
/creativekeys subtract <targets> <seconds> # Remove time from timer
```

### Examples
```bash
# Give a Creative Key to all players
/creativekeys give @a

# Pause timer for specific player
/creativekeys pause TatosTV

# Add 5 minutes (300 seconds) to a player's timer
/creativekeys add TatosTV 300

# Reset timer for all online players
/creativekeys reset @a
```

## Installation

### Requirements
- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.205 or later
- **Java**: 21 (shipped with Minecraft 1.21.1)

### Steps
1. Download the latest `.jar` file from releases
2. Place in your `mods/` folder
3. Launch Minecraft with NeoForge profile
4. Find Creative Keys in the Tools & Utilities creative tab

## Configuration

Config file: `config/creative_keys-common.toml`

```toml
# Whether the Creative Key item is enabled
enableCreativeKey = true

# Optional list of tags or notes associated with the key  
keyTags = []
```

## Technical Details

### Item Properties
- **Duration**: 30 minutes (36,000 ticks) per key
- **Stackable**: Yes, multiple keys extend duration
- **Creative Tab**: Custom tab + Tools & Utilities
- **Visual**: Golden key with enchantment glint

### Data Storage
- Uses player persistent NBT data
- Survives server restarts and player reconnections
- Network synchronized for client-side HUD

### Performance
- Minimal server impact (single player tick event)
- Client-side HUD rendering only when active
- No world modifications or persistent data files

## Development

### Building from Source
```bash
git clone <repository-url>
cd creative-keys
./gradlew build
```

Built JARs will be in `build/libs/`

### Project Structure
```
src/main/java/com/tatostv/creativekeys/
├── CreativeKeys.java           # Main mod class
├── ModConfigs.java            # Configuration handling
├── item/CreativeKeyItem.java  # Core item logic
├── command/                   # Admin commands
├── client/                    # HUD overlays
├── network/                   # Client sync
└── core/                      # Event handlers
```

### API Usage
The mod uses standard NeoForge APIs:
- `DeferredRegister` for item/tab registration
- `PlayerTickEvent` for timer management  
- `CustomPacketPayload` for client s