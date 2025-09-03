# Contributing to Creative Keys

Thank you for considering contributing to Creative Keys! This document provides guidelines and information for contributors.

## 🤝 How to Contribute

### Reporting Issues
- **Use the Issue Template**: When reporting bugs, please provide as much detail as possible
- **Include System Info**: Minecraft version, NeoForge version, Java version, and OS
- **Steps to Reproduce**: Clear, step-by-step instructions to reproduce the issue
- **Expected vs Actual**: What you expected to happen vs what actually happened
- **Screenshots/Logs**: Include relevant screenshots or log files when applicable

### Suggesting Features
- **Check Existing Issues**: Search existing issues to avoid duplicates
- **Use Case Description**: Explain why this feature would be useful
- **Implementation Ideas**: If you have ideas about how it could be implemented
- **Backward Compatibility**: Consider how it affects existing functionality

### Code Contributions

#### Development Setup
1. **Fork the Repository**: Create a fork of the Creative Keys repository
2. **Clone Locally**: `git clone https://github.com/yourusername/creative-keys.git`
3. **Set Up Development Environment**:
   ```bash
   cd creative-keys
   ./gradlew runClient  # Test client-side
   ./gradlew runServer  # Test server-side
   ```

#### Code Standards

##### Java Code Style
- **Indentation**: 4 spaces (no tabs)
- **Line Length**: Maximum 120 characters
- **Naming Conventions**:
  - Classes: `PascalCase` (e.g., `CreativeKeyItem`)
  - Methods/Variables: `camelCase` (e.g., `expireCreativeMode`)
  - Constants: `UPPER_SNAKE_CASE` (e.g., `NBT_EXPIRES`)
  - Packages: `lowercase` (e.g., `com.tatostv.creativekeys`)

##### Documentation
- **Javadoc Comments**: Required for all public methods and classes
- **Inline Comments**: Explain complex logic and non-obvious behavior
- **README Updates**: Update documentation for new features

##### Code Structure
```java
/**
 * Brief description of what this class/method does.
 * 
 * @param parameter Description of parameter
 * @return Description of return value
 * @since version when this was added
 */
public ReturnType methodName(ParameterType parameter) {
    // Implementation with clear, readable code
    try {
        // Main logic here
        return result;
    } catch (Exception e) {
        // Proper error handling
        LOGGER.error("Error in methodName: {}", e.getMessage());
        return defaultValue;
    }
}
```

#### Testing Guidelines

##### Required Testing
- **Single Player**: Test all functionality in single-player worlds
- **Multiplayer**: Test on dedicated servers with multiple players
- **Edge Cases**: Test with unusual scenarios (server restart, player disconnect, etc.)
- **Performance**: Ensure changes don't impact server performance

##### Test Scenarios
```
1. Basic Functionality:
   - Right-click Creative Key activates Creative Mode
   - Timer displays correctly on HUD
   - Timer expires and reverts to Survival

2. Command Testing:
   - All commands work with proper permissions
   - Error messages appear for invalid usage
   - Bulk operations work with player selectors

3. Edge Cases:
   - Server restart while timer is active
   - Player disconnect/reconnect with active timer
   - Multiple keys used simultaneously
   - Commands used on offline players
```

#### Pull Request Guidelines

##### Before Submitting
- [ ] Code follows project style guidelines
- [ ] All existing tests still pass
- [ ] New functionality includes appropriate tests
- [ ] Documentation has been updated
- [ ] Commit messages are clear and descriptive

##### PR Description Template
```markdown
## Description
Brief description of changes made.

## Type of Change
- [ ] Bug fix (non-breaking change that fixes an issue)
- [ ] New feature (non-breaking change that adds functionality)
- [ ] Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] Documentation update

## Testing Done
- [ ] Single-player testing
- [ ] Multiplayer testing
- [ ] Command testing
- [ ] Edge case testing

## Screenshots/Videos
Include any relevant media showing the changes.

## Additional Notes
Any additional context or notes for reviewers.
```

## 🏗️ Development Workflow

### Branch Naming
- **Feature branches**: `feature/description-of-feature`
- **Bug fixes**: `bugfix/issue-number-description`
- **Documentation**: `docs/what-was-updated`

### Commit Messages
```
feat: add pause/resume timer functionality
fix: resolve inventory clearing config issue
docs: update README with new command examples
refactor: improve error handling in timer system
```

### Version Management
- **Semantic Versioning**: We follow [SemVer](https://semver.org/)
- **Version Format**: `MAJOR.MINOR.PATCH`
  - `MAJOR`: Breaking changes
  - `MINOR`: New features (backward compatible)
  - `PATCH`: Bug fixes (backward compatible)

## 🧪 Development Environment

### Required Tools
- **Java 21**: OpenJDK 21 or Oracle Java 21
- **Git**: For version control
- **IDE**: IntelliJ IDEA (recommended) or Eclipse
- **Gradle**: Included wrapper (`./gradlew`)

### IDE Setup

#### IntelliJ IDEA
1. **Import Project**: Open the `build.gradle` file as a project
2. **JDK Configuration**: Set Project SDK to Java 21
3. **Code Style**: Import `idea/codeStyles/Default.xml` if provided
4. **Run Configurations**: Use the Gradle tasks for running client/server

#### Visual Studio Code
1. **Java Extension Pack**: Install the Java extension pack
2. **Gradle Support**: Install Gradle Language Support extension
3. **Configure JDK**: Set `java.home` to Java 21 installation

### Useful Gradle Tasks
```bash
# Build the mod
./gradlew build

# Run client for testing
./gradlew runClient

# Run server for testing
./gradlew runServer

# Generate data (recipes, models, etc.)
./gradlew runData

# Clean build artifacts
./gradlew clean
```

## 📋 Code Review Process

### What Reviewers Look For
1. **Code Quality**: Clean, readable, well-documented code
2. **Functionality**: Features work as intended
3. **Performance**: No negative impact on server performance
4. **Compatibility**: Maintains backward compatibility when possible
5. **Testing**: Adequate testing has been performed

### Review Response Guidelines
- **Be Respectful**: Reviewers are volunteers helping improve the project
- **Address Feedback**: Respond to all review comments
- **Ask Questions**: Don't hesitate to ask for clarification
- **Learn and Improve**: Use feedback to improve future contributions

## 🚀 Release Process

### Feature Release Cycle
1. **Development**: Features developed in feature branches
2. **Testing**: Thorough testing in development environment
3. **Code Review**: All changes reviewed by maintainers
4. **Integration**: Merged to main branch
5. **Release**: Tagged release with changelog

### Hotfix Process
1. **Critical Bug Identified**: High-priority issues that need immediate fixes
2. **Hotfix Branch**: Created from latest release tag
3. **Quick Fix**: Minimal changes to resolve the issue
4. **Emergency Release**: Fast-tracked testing and release

## 🏆 Recognition

### Contributors
All contributors will be:
- **Listed in CONTRIBUTORS.md**: Permanent recognition of contributions
- **Mentioned in Release Notes**: Credit for specific features/fixes
- **GitHub Profile**: Contributions show on your GitHub profile
- **Community Recognition**: Acknowledged in community discussions

### Types of Contributions Valued
- **Code Contributions**: New features, bug fixes, performance improvements
- **Documentation**: README updates, code comments, tutorials
- **Testing**: Bug reports, feature testing, edge case discovery
- **Design**: UI/UX improvements, texture work, model improvements
- **Community Support**: Helping users, answering questions

## 📞 Getting Help

### Development Questions
- **GitHub Issues**: For bugs and feature requests
- **GitHub Discussions**: For general development questions
- **Discord**: Join the NeoForge community Discord

### Maintainer Contact
- **Primary Maintainer**: TatosTV
- **Response Time**: Typically within 48 hours
- **Best Practices**: Use GitHub issues for trackable requests

---

Thank you for contributing to Creative Keys! Your help makes this project better for everyone in the Minecraft community.