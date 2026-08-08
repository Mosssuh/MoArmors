# MoArmors

> A fully configurable armor pieces plugin for Spigot/Paper servers.

MoArmors allows server owners to create completely customizable armor pieces with their own levels, experience system, actions, rewards, variables, tags, upgrades and boosters.

Every armor piece can react to Minecraft events, execute commands, apply potion effects, level up, unlock new abilities and much more.

---

## Features

* Unlimited custom armor sets
* Helmet, Chestplate, Leggings and Boots support
* Level & Experience system
* Upgrade system
* Configurable actions and rewards
* PlaceholderAPI support
* Public API and Events
* Variables system
* Tags system
* Fully configurable

---

## Installation

### Requirements

| Plugin         | Required |
| -------------- | :------: |
| MoCore         |     ✅    |
| NBTAPI         |     ✅    |
| PlaceholderAPI |     ❌    |
| MoBoosters     |     ❌    |

> PlaceholderAPI and MoBoosters are optional integrations.

---

# API

MoArmors includes a public API that can be used by other plugins.

## Main Class

```text
ArmorsAPI
```

## Events

| Event                    | Description                                             |
| ------------------------ | ------------------------------------------------------- |
| `PieceChangeExpEvent`    | Called whenever an armor piece changes experience.      |
| `PieceChangeLevelEvent`  | Called whenever an armor piece changes level.           |
| `PlayerChangePieceEvent` | Called whenever a player changes an active armor piece. |

---

# PlaceholderAPI

MoArmors provides PlaceholderAPI placeholders for accessing armor pieces, levels, experience, variables, tags and boosters.

## Piece Types

The following piece types are supported:

```text
HELMET
CHESTPLATE
LEGGINGS
BOOTS
ALL
```

## Active Armor

| Placeholder                                                                           | Description                                                |
| ------------------------------------------------------------------------------------- | ---------------------------------------------------------- |
| `%moarmors_active_<piece type>_code%`                                                 | Returns the armor piece code.                              |
| `%moarmors_active_<piece type>_level%`                                                | Returns the armor piece level.                             |
| `%moarmors_active_<piece type>_exp%`                                                  | Returns the armor piece experience.                        |
| `%moarmors_active_<piece type>_cost%`                                                 | Returns the upgrade cost.                                  |
| `%moarmors_active_<piece type>_max_level%`                                            | Returns the maximum level.                                 |
| `%moarmors_active_<piece type>_tags%`                                                 | Returns the armor piece tags.                              |
| `%moarmors_active_<piece type>_contains_tag_<tag>%`                                   | Checks whether the armor piece contains the specified tag. |
| `%moarmors_active_<piece type>_variable_{<variable>}%`                                | Returns an armor piece variable.                           |
| `%moarmors_active_<piece type>_has_variable_{<variable>}%`                            | Checks whether the armor piece has the specified variable. |
| `%moarmors_active_<piece type>_boost_<booster type>_<applicator type>_<boosted>%`     | Returns booster information.                               |
| `%moarmors_active_<piece type>_has_boost_<booster type>_<applicator type>_<boosted>%` | Checks whether the booster exists.                         |

---

# Variables

MoArmors provides several groups of variables that can be used inside actions, requirements, rewards and item configurations.

## Player

```text
%player%
```

## Cooldown

```text
%cooldown%
%cooldown_formatted%
```

## ItemStack

```text
%itemstack_material%
%itemstack_data%
%itemstack_name%
%itemstack_lore%
%itemstack_amount%
%itemstack_durability%
```

### MoArmors Item Variables

```text
%itemstack_is_piece%
%itemstack_piece_level%
%itemstack_piece_exp%
%itemstack_piece_cost%
%itemstack_piece_max_level%
%itemstack_piece_tags%
%itemstack_piece_code%
%itemstack_piece_variable_{<variable>}%
```

## Entity

```text
%entity_type%
%entity_name%
%entity_x%
%entity_y%
%entity_z%
%entity_world%
```

## Block

```text
%block%
%block_data%
%block_x%
%block_y%
%block_z%
%block_world%
```

## Piece

Piece variables use the following format:

```text
%<piece type>_level%
%<piece type>_exp%
%<piece type>_cost%
%<piece type>_max_level%
%<piece type>_tags%
%<piece type>_code%
%<piece type>_variable_{<variable>}%
```

### Example

```text
%helmet_level%
%helmet_exp%
%helmet_code%
%helmet_variable_{tokens_generator}%
```

---

# Experience System

Armor pieces can gain experience automatically through configurable Minecraft events.

## Supported Experience Types

| Type           | Description                          |
| -------------- | ------------------------------------ |
| `BLOCK_BREAK`  | Gain experience by breaking blocks.  |
| `BLOCK_PLACE`  | Gain experience by placing blocks.   |
| `PLAYER_FISH`  | Gain experience while fishing.       |
| `PLAYER_KILLS` | Gain experience by killing entities. |

### Configuration Example

```yaml
exp:
  PLAYER_KILLS:
    - 'ZOMBIE -> 2'

  BLOCK_BREAK:
    - 'ALL -> 2'
```

> When `ALL` is used, every entity or block involved in the event can provide experience.

---

# Actions

Actions define what an armor piece does whenever a specific Minecraft event occurs.

An action can contain:

* Event
* Cooldown
* Cooldown Message
* Cooldown Bypass
* Requirements
* Rewards
* Else Rewards
* Cancel Action

## Action Structure

```yaml
default:

  SpeedBoost:

    event: PLAYER_KILLS

    requirements:
      - '[EVENT] ZOMBIE'
      - '[EVAL] %level% >= 5'

    rewards:
      - '[CHANCE -> 50] EFFECT -> SPEED::5::1'
```

---

## Event Types

Each event provides different variables depending on its context.

| Event Type             | Available Variables                 |
| ---------------------- | ----------------------------------- |
| `BLOCK_BREAK`          | Block Variables                     |
| `BLOCK_PLACE`          | Block Variables                     |
| `BLOCK_INTERACT`       | Block Variables                     |
| `PLAYER_ATTACK`        | Entity Variables                    |
| `PLAYER_ATTACKED`      | Entity Variables                    |
| `PLAYER_KILLS`         | Entity Variables                    |
| `PLAYER_DIE`           | Entity Variables                    |
| `PLAYER_CAUGHT_FISH`   | Entity Variables                    |
| `PLAYER_CAUGHT_ENTITY` | Entity Variables                    |
| `PLAYER_BED_ENTER`     | None                                |
| `PLAYER_BED_LEAVE`     | None                                |
| `PLAYER_CHANCE_WORLD`  | `%from_world%`, `%to_world%`        |
| `PLAYER_COMMAND`       | `%command%`                         |
| `PLAYER_JOIN`          | None                                |
| `PLAYER_LEAVE`         | None                                |
| `PLAYER_RESPAWN`       | None                                |
| `PLAYER_FLY`           | None                                |
| `PLAYER_UNFLY`         | None                                |
| `PLAYER_SNEAK`         | None                                |
| `PLAYER_UNSNEAK`       | None                                |
| `PLAYER_SPRINT`        | None                                |
| `PLAYER_UNSPRINT`      | None                                |
| `PLAYER_EQUIP_PIECE`   | `%piece_type%`, ItemStack Variables |
| `PLAYER_UNEQUIP_PIECE` | `%piece_type%`, ItemStack Variables |
| `PLAYER_LEVELUP`       | `%old_level%`, `%new_level%`        |
| `PLAYER_CHAT`          | `%message%`                         |
| `ITEM_SELECT`          | ItemStack Variables                 |
| `ITEM_UNSELECT`        | ItemStack Variables                 |
| `ITEM_ENCHANT`         | `%exp_cost%`, ItemStack Variables   |
| `ITEM_CRAFT`           | ItemStack Variables                 |
| `ITEM_INTERACT`        | `%click_type%`, ItemStack Variables |
| `ITEM_CONSUME`         | ItemStack Variables                 |
| `ITEM_BREAK`           | ItemStack Variables                 |
| `ITEM_PICKUP`          | ItemStack Variables                 |
| `ITEM_DROP`            | ItemStack Variables                 |
| `ITEM_HELD`            | ItemStack Variables                 |
| `ITEM_UNHELD`          | ItemStack Variables                 |
| `ENTITY_INTERACT`      | Entity Variables                    |

---

# Requirements

Requirements determine whether an action should be executed.

If all requirements succeed, the configured rewards are executed. If the requirements fail, the `else` section is executed when present.

## Requirement Types

| Type    | Description                                             |
| ------- | ------------------------------------------------------- |
| `EVENT` | Checks information related to the current event.        |
| `EVAL`  | Evaluates expressions using placeholders and variables. |

## EVAL

`EVAL` supports:

* PlaceholderAPI
* Event Variables
* Local Variables
* Item Variables
* Piece Variables
* Cooldown Variables

### String Operators

```text
equals
!equals
equalsIgnoreCase
!equalsIgnoreCase
startsWith
!startsWith
contains
!contains
```

### Number Operators

```text
<
<=
>
>=
==
!=
```

### Example

```yaml
requirements:
  - '[EVENT] ZOMBIE'
  - '[EVAL] %level% >= 10'
```

Multiple requirements can be separated using `||`.

---

# Rewards

Rewards are executed after the requirements have been successfully completed.

Each reward can also use:

```text
CHANCE
CHANCE_PER_LEVEL
```

## Commands

| Reward                 | Format                              |
| ---------------------- | ----------------------------------- |
| `CONSOLE_COMMAND`      | `CONSOLE_COMMAND -> <command>`      |
| `PLAYER_COMMAND`       | `PLAYER_COMMAND -> <command>`       |
| `PLAYER_COMMAND_AS_OP` | `PLAYER_COMMAND_AS_OP -> <command>` |

## Messages

| Reward              | Format                                   |
| ------------------- | ---------------------------------------- |
| `TITLE`             | `TITLE -> <title>::<subtitle>`           |
| `SOUND`             | `SOUND -> <sound>::<volume>::<pitch>`    |
| `BROADCAST_MESSAGE` | `BROADCAST_MESSAGE -> <message>`         |
| `BROADCAST_TITLE`   | `BROADCAST_TITLE -> <title>::<subtitle>` |
| `JSON`              | `JSON -> <json>`                         |
| `JSON_BROADCAST`    | `JSON_BROADCAST -> <json>`               |

## Effects

| Reward   | Format                                        |
| -------- | --------------------------------------------- |
| `EFFECT` | `EFFECT -> <effect>::<duration>::<amplifier>` |

Example:

```text
EFFECT -> SPEED::10::1
```

## Events

| Reward         | Format         |
| -------------- | -------------- |
| `CANCEL_EVENT` | `CANCEL_EVENT` |
| `CANCEL_DROPS` | `CANCEL_DROPS` |

## Actions

| Reward           | Format                       |
| ---------------- | ---------------------------- |
| `EXECUTE_ACTION` | `EXECUTE_ACTION -> <action>` |

Executes another action registered in the same armor configuration.

## Experience

| Reward       | Format                             |
| ------------ | ---------------------------------- |
| `ADD_EXP`    | `ADD_EXP -> <exp>`    |
| `SET_EXP`    | `SET_EXP -> <exp>`    |
| `REMOVE_EXP` | `REMOVE_EXP -> <exp>` |

Example:

```text
ADD_EXP -> 100
SET_EXP -> 500
REMOVE_EXP -> 50
```

## Levels

| Reward         | Format                                 |
| -------------- | -------------------------------------- |
| `ADD_LEVEL`    | `ADD_LEVEL -> <level>`    |
| `SET_LEVEL`    | `SET_LEVEL -> <level>`    |
| `REMOVE_LEVEL` | `REMOVE_LEVEL -> <level>` |

Example:

```text
ADD_LEVEL -> 1
SET_LEVEL -> 10
REMOVE_LEVEL -> 2
```

## Variables

| Reward            | Format                                                 |
| ----------------- | ------------------------------------------------------ |
| `SET_VARIABLE`    | `SET_VARIABLE -> <variable>::<value>`    |
| `REMOVE_VARIABLE` | `REMOVE_VARIABLE -> <variable>::<value>` |

Example:

```text
SET_VARIABLE -> tokens_generator::1
```

---

# Reward Examples

```text
[CHANCE -> 100] CONSOLE_COMMAND -> say Hello World

[CHANCE -> 50] EFFECT -> SPEED::10::1

[CHANCE -> 100] EXECUTE_ACTION -> SpeedBoost

[CHANCE_PER_LEVEL -> 2] ADD_EXP -> HELMET::50

[CHANCE -> 100] TITLE -> &aArmor Level Up!::&7Congratulations!
```

## Chained Rewards

Rewards can be chained using `&&`.

```text
[CHANCE -> 100] EFFECT -> SPEED::5::1 && MESSAGE -> Speed activated!
```

Multiple outcomes can be separated using `||`.

```text
[CHANCE -> 50] EFFECT -> SPEED::5::1 && MESSAGE -> Lucky! || MESSAGE -> Better luck next time!
```

---

# Armor Configuration

Every armor configuration uses a unique code to identify the armor set.

## Basic Structure

```yaml
ExampleArmor:
```

### Example

```yaml
TokensArmor:
```

---

## Tags

Tags are used to categorize armor sets.

```yaml
tags:
  - Combat
  - PvP
  - Legendary
```

Tags can be checked using PlaceholderAPI or action requirements.

---

## Variables

Variables allow custom information to be stored inside each armor piece.

```yaml
variables:
  - damage -> 15
  - rarity -> legendary
```

Variables can be accessed through Piece Variables.

Example:

```text
%helmet_variable_{damage}%
```

---

# Upgrade System

Each armor configuration can define its own progression.

## Configuration

```yaml
upgrades:

  max-level: 10
  cost-per-level: 100

  message:

    progress:
      - '&7Progress: %progress%'

    maxed-progress:
      - '&aMAX LEVEL'
```

## Options

| Option           | Description                                  |
| ---------------- | -------------------------------------------- |
| `max-level`      | Maximum level the armor can reach.           |
| `cost-per-level` | Upgrade cost formula.                        |
| `progress`       | Displayed while leveling.                    |
| `maxed-progress` | Displayed once the maximum level is reached. |

---

# Pieces Information

Each armor configuration can define information for its individual pieces.

## Supported Pieces

```text
HELMET
CHESTPLATE
LEGGINGS
BOOTS
```

## Configuration

```yaml
pieces-info:

  HELMET:

    material: DIAMOND_HELMET
    data: 0

    name: '&e&lTokens &f❙ &fHelmet'

    lore:
      - ''
      - '&7&o(( Equip this piece and receive'
      - '&7&oboost of tokens ))'
      - ''
      - '&e&lINFO'
      - '&e ❙ &fLevel: &a%level%&7/&a%max_level%'
      - ''
      - '&e&lBOOSTS'
      - '&e ❙ &fBoost: &e+{math_%level%/200}'
      - ''
      - '%progress%'
      - ''

    unbreakable: true
    unique: true

    enchantments:
      - LUCK:2

    flags:
      - HIDE_ENCHANTS
```

## Available Options

| Option         | Description                                               |
| -------------- | --------------------------------------------------------- |
| `material`     | Minecraft material, custom head or colored leather armor. |
| `data`         | Legacy durability/data value.                             |
| `name`         | Item display name.                                        |
| `lore`         | Item description.                                         |
| `unbreakable`  | Makes the armor piece unbreakable.                        |
| `unique`       | Prevents the item from stacking with identical items.     |
| `enchantments` | Item enchantments.                                        |
| `flags`        | Item flags.                                               |

---

# Materials

MoArmors supports standard Minecraft materials, custom heads and colored leather armor.

## Normal Material

```yaml
material: DIAMOND_HELMET
```

## Custom Head

```yaml
material: basehead-<base64>
```

---

# Armor Actions

Each armor configuration can define one or more actions.

## Configuration

```yaml
actions:

  default:

    SpeedBoost:

      event: PLAYER_KILLS

      cooldown: 5
      cooldown_bypass: false
      cooldown_message: "&cWait %cooldown_formatted%!"

      requirements:
        - '[EVENT] ZOMBIE'
        - '[EVAL] %level% >= 5'

      rewards:
        - '[CHANCE -> 50] EFFECT -> SPEED::5::1'

      else:
        - '[CHANCE -> 100] MESSAGE -> &cRequirements not met.'
```

## Available Options

| Option             | Description                                      |
| ------------------ | ------------------------------------------------ |
| `event`            | Minecraft event that triggers the action.        |
| `cooldown`         | Cooldown in seconds.                             |
| `cooldown_bypass`  | Executes rewards even if the cooldown is active. |
| `cooldown_message` | Message shown while on cooldown.                 |
| `requirements`     | Conditions required before executing rewards.    |
| `rewards`          | Rewards executed when requirements succeed.      |
| `else`             | Rewards executed when requirements fail.         |
| `cancel_action`    | Cancels the current action when enabled.         |

---

# Boosters

MoArmors supports **MoBoosters** integration.

## Multiplier Types

| Type              | Description                                       |
| ----------------- | ------------------------------------------------- |
| `BASE`            | Always provides the configured boost.             |
| `BOOST_PER_LEVEL` | The boost is multiplied by the armor piece level. |

## Configuration Example

```yaml
boosters:

  ExampleBoostName:

    multiplier: BOOST_PER_LEVEL
    type: PERSONAL
    applicator: MINECRAFT
    boosted: experience
    boost: 3
```

> To use boosters, the **MoBoosters** plugin must be installed on your server.

---

## Booster Identifier

An armor configuration can define a booster identifier.

```yaml
booster-identifier: ExampleBoost
```

The identifier ensures that only one booster with the same identifier is applied to certain items.

The identifier must be registered in the MoBoosters configuration.

For MoArmors, the booster must use:

```text
Applicator: MoArmors
```

The current supported boosted type for this integration is experience.

> If you want all boosters with the same applicator type to be considered, remove `booster-identifier`.

---

# Complete Example

The following example demonstrates a complete armor configuration using the main MoArmors features.

```yaml
TokensArmor:

  tags:
    - Tokens
    - Armor

  variables:
    - 'tokens_generator -> 0'

  upgrades:

    max-level: 50
    cost-per-level: 5000

    message:

      progress:
        - '&e ❙ &fProgress: &a%exp%&7/&a%cost%'

      maxed-progress:
        - '&e ❙ &fProgress: &c&lMAX LEVEL'

  exp:

    BLOCK_BREAK:
      - 'ALL -> 1'

  pieces-info:

    HELMET:

      material: DIAMOND_HELMET
      data: 0

      name: '&e&lTokens &f❙ &fHelmet'

      lore:
        - ''
        - '&7&o(( Equip this piece and receive'
        - '&7&oboost of tokens ))'
        - ''
        - '&e&lINFO'
        - '&e ❙ &fLevel: &a%level%&7/&a%max_level%'
        - ''
        - '&e&lBOOSTS'
        - '&e ❙ &fBoost: &e+{local_math_%level%/200}'
        - ''
        - '%progress%'
        - ''

    CHESTPLATE:

      material: DIAMOND_CHESTPLATE
      data: 0

      name: '&e&lTokens &f❙ &fChestplate'

      lore:
        - ''
        - '&7&o(( Equip this piece and receive'
        - '&7&oboost of tokens ))'
        - ''
        - '&e&lINFO'
        - '&e ❙ &fLevel: &a%level%&7/&a%max_level%'
        - ''
        - '%progress%'
        - ''

    LEGGINGS:

      material: DIAMOND_LEGGINGS
      data: 0

      name: '&e&lTokens &f❙ &fLeggings'

      lore:
        - ''
        - '&7&o(( Equip this piece and receive'
        - '&7&oboost of tokens ))'
        - ''
        - '&e&lINFO'
        - '&e ❙ &fLevel: &a%level%&7/&a%max_level%'
        - ''
        - '%progress%'
        - ''

    BOOTS:

      material: DIAMOND_BOOTS
      data: 0

      name: '&e&lTokens &f❙ &fBoots'

      lore:
        - ''
        - '&7&o(( Equip this piece and receive'
        - '&7&oboost of tokens ))'
        - ''
        - '&e&lINFO'
        - '&e ❙ &fLevel: &a%level%&7/&a%max_level%'
        - ''
        - '%progress%'
        - ''

  actions:

    default:

      HelmetAbilityActivator:

        event: BLOCK_BREAK

        requirements:
          - '[EVAL] %helmet_code% equals TokensArmor'
          - '[EVAL] %helmet_variable_{tokens_generator}% == 0'
          - '[EVAL] %helmet_level% == 25'

        rewards:
          - '[CHANCE -> 100] SET_VARIABLE -> tokens_generator::1 && MESSAGE -> &e&lABILITY! &aYour helmet unlocked Tokens Generator ability.'

  boosters:

    Tokens:

      multiplier: BOOST_PER_LEVEL
      type: PERSONAL
      applicator: EDPRISON
      boosted: TOKENS
      boost: 0.005
```

