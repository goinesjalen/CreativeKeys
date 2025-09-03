# Creative Keys

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.txt)
[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.205%2B-orange)](https://neoforged.net/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green)](https://minecraft.net/)
[![Java](https://img.shields.io/badge/Java-21-blue)](https://adoptium.net/)

A lightweight NeoForge mod that adds Creative Key items for server recordings and content creation. Perfect for content creators who need temporary creative mode access with professional timer displays and advanced management features.

## ✨ Features

### 🔑 Creative Key Item
- **Beautiful Visual Design**: Golden key with enchanted glint and animated rainbow text
- **30 Minutes Per Key**: Each key grants exactly 30 minutes of Creative Mode
- **Stackable Duration**: Use multiple keys to extend your creative time
- **Smart Activation**: Automatically detects and extends existing timers

### 📊 Professional HUD Display
- **Clean Timer Overlay**: Shows remaining time in `M:SS` format with key icon
- **Non-Intrusive Design**: Positioned above hotbar with subtle transparency
- **Real-Time Updates**: Live countdown with smooth animations
- **Pause Indicator**: Clear visual feedback when timer is paused

### ⏸️ Advanced Timer Controls
- **Pause/Resume System**: Full control over timer state for content creation
- **Admin Commands**: Comprehensive command suite for server management
- **Persistent Data**: Timer survives server restarts and player reconnections
- **Flexible Management**: Add, subtract, or reset time as needed

### ⚙️ Server Administration
- **Permission-Based**: All commands require OP level 2 permissions
- **Bulk Operations**: Target multiple players with selector support
- **Configuration Options**: Toggle inventory clearing and other behaviors
- **Error Handling**: Robust system with comprehensive error messages

## 🚀 Installation

### Requirements
- **Minecraft**: 1.21.1
- **NeoForge**: 21.1.205 or later
- **Java**: 21 (bundled with Minecraft 1.21.1)

### Installation Steps
1. Download the latest `.jar` file from the [Releases](../../releases) page
2. Place the file in your server's `mods/` folder
3. Restart your server or launch Minecraft with NeoForge
4. Creative Keys will appear in the Tools & Utilities creative tab

## 📖 Usage Guide

### Basic Usage
```
1. Obtain a Creative Key from creative menu or admin commands
2. Right-click to activate 30 minutes of Creative Mode
3. Watch the HUD timer count down your remaining time
4. Timer automatically reverts to Survival when expired
```

### For Content Creators
The mod is designed with content creation in mind:
- **Professional Appearance**: Clean, modern HUD design
- **Flexible Controls**: Pause recording without losing time
- **Reliable Operation**: No unexpected crashes or glitches
- **Minimal Footprint**: Lightweight with zero performance impact

## 🔧 Commands Reference

All commands require **OP level 2** and support player selectors (`@a`, `@p`, etc.)

### Core Commands
```bash
# Give Creative Key to players
/creativekeys give <targets>

# Timer Control
/creativekeys pause <targets>    # Pause active timer
/creativekeys resume <targets>   # Resume paused timer
/creativekeys reset <targets>    # Clear timer completely

# Time Adjustment
/creativekeys add <targets> <seconds>       # Add time to timer
/creativekeys subtract <targets> <seconds>  # Remove time from timer
```

### Configuration Commands
```bash
# Toggle inventory clearing on expiration
/creativekeys clearInventory <true|false>

# Check current configuration status
/creativekeys status
```

### Command Examples
```bash
# Give keys to all online players
/creativekeys give @a

# Pause timer for specific content creator
/creativekeys pause TatosTV

# Add 10 minutes (600 seconds) to player's timer
/creativekeys add @p 600

# Reset all active timers on server
/creativekeys reset @a

# Disable inventory clearing for gentler experience
/creativekeys clearInventory false
```

## ⚙️ Configuration

Config file: `config/creative_keys-common.toml`

```toml
# Creative Keys Configuration

# Whether to clear player inventory when Creative mode expires
# true = Clear all items (default, prevents item duplication)
# false = Keep inventory (gentler for creative building)
clearInventoryOnExpire = true
```

**Note**: Configuration can also be modified in-game using the `/creativekeys clearInventory` command.

## 🎨 Visual Features

### Item Design
- **Golden Key Model**: Professional, recognizable icon
- **Enchantment Glint**: Magical appearance with subtle shimmer
- **Rainbow Text**: Animated color-shifting name (client-side only)
- **Custom Tooltip**: Clear usage instructions

### HUD Elements
- **Timer Display**: Clean `M:SS` format with key icon
- **Pause State**: Grayed out `(paused)` indicator
- **Positioning**: Above hotbar, left-aligned for consistency
- **Transparency**: Semi-transparent background for visibility

## 🔧 Technical Details

### Performance
- **Minimal Server Impact**: Single tick event handler
- **Efficient Networking**: Only syncs when necessary
- **Client-Side Rendering**: HUD rendered locally for smooth performance
- **Memory Usage**: Under 1MB total footprint

### Data Storage
- **NBT Persistence**: Uses player persistent data
- **Server Restart Safe**: Data survives server reboots
- **Network Sync**: Automatic client synchronization
- **No File I/O**: No external files or databases

### Compatibility
- **Mod Friendly**: No conflicts with other mods
- **Vanilla Compatible**: Works on vanilla clients (server-side only)
- **Version Flexible**: Supports NeoForge 21.1.205+
- **Clean Uninstall**: Removes all data when mod is removed

## 🛠️ Development

### Building from Source
```bash
git clone https://github.com/TatosTV/creative-keys.git
cd creative-keys
./gradlew build
```

Built JARs will be in `build/libs/`

### Development Setup
```bash
# Run client for testing
./gradlew runClient

# Run server for testing
./gradlew runServer

# Generate development data
./gradlew runData
```

### Project Structure
```
src/main/java/com/tatostv/creativekeys/
├── CreativeKeys.java              # Main mod class & registration
├── ModConfigs.java               # Configuration handling
├── item/CreativeKeyItem.java     # Core item logic & mechanics
├── command/                      # Admin command system
│   └── CreativeKeysCommand.java  # All /creativekeys commands
├── client/                       # Client-side rendering
│   ├── ClientOverlays.java       # HUD registration
│   ├── CreativeKeyHudOverlay.java # Active timer display
│   └── PausedKeyHudOverlay.java   # Paused timer display
├── network/                      # Client-server sync
│   ├── NetworkMessages.java      # Message registration
│   ├── SyncExpiresPayload.java   # Timer sync packet
│   └── SyncPausedRemainingPayload.java # Pause sync packet
└── core/                         # Event handling
    └── CreativeKeysEvents.java   # Player tick & login events
```

### API Integration
The mod uses standard NeoForge APIs:
- **DeferredRegister**: Item and creative tab registration
- **PlayerTickEvent**: Server-side timer management
- **CustomPacketPayload**: Client-server data synchronization
- **CommandDispatcher**: Administrative command system

## 🤝 Contributing

We welcome contributions! Please feel free to:
- Report bugs via [Issues](../../issues)
- Suggest features or improvements
- Submit pull requests for bug fixes
- Help improve documentation

### Development Guidelines
- Follow existing code style and conventions
- Test changes thoroughly in both single-player and multiplayer
- Update documentation for new features
- Maintain backward compatibility when possible

## 📄 License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

### What This Means
- ✅ Commercial use allowed
- ✅ Modification and distribution allowed
- ✅ Private use allowed
- ✅ Include original license and copyright notice

## 🙋 Support

### Getting Help
- **Issues**: Report bugs via [GitHub Issues](../../issues)
- **Documentation**: Check this README and in-game tooltips
- **Community**: Join NeoForge community discussions

### Common Issues
**Q: Timer not showing on HUD?**
A: Make sure you're running the mod on both client and server. The HUD only appears when timer is active.

**Q: Commands not working?**
A: Commands require OP level 2 permissions. Use `/op <username>` to grant permissions.

**Q: Mod not loading?**
A: Verify you have NeoForge 21.1.205+ and Java 21. Check the logs for error messages.

### System Requirements
- **Server RAM**: 2GB+ recommended for smooth operation
- **Java Version**: OpenJDK 21 or Oracle Java 21
- **Operating System**: Any system supporting Minecraft server

---

**Created by TatosTV** | Built for content creators and server administrators | **[Download Now](../../releases)**