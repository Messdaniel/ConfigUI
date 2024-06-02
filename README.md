# ConfigUI
A simple plugin where you can create GUIs through the use of YAML (YML) files.

## Features
 - Easy GUI Creation: Define your GUIs using easy-to-read YAML files.
 - Customizable Components: Add buttons, labels, text fields, and more.
 - Event Handling: Configure actions for Clicking, Opening and Closing.
 - Full customization: Everything that can be customized is customizable.

## Example 
You can find the example gui in the "example.yml" in the directory "guis".

## How to create a gui 
To create a gui simply create a new yml file in the directory "guis", the name of the gui is the name of the file. 
Look in the gui "example.yml" for how it should look.

When you are done configuration the gui you run the command /uireload to reload the plugin.

To open the gui you use the command /uiopengui {name of gui}

## Commands
/uireload is the command to reload the plugin after you have done some changes to a gui or config.yml.
The permission needed to use the command is "configui.reload".

/uiopengui is the command used to open a gui.
The permission needed to use the command is "configui.openmenu".

## Documentation
### Gui - section

#### `title` is used to set the name of the gui.
#### `size` is used to set the size of the gui (9 ,18, 27, 36, 45, 54).
#### `disable-moving-items` is used to set if items can be moved around in the inventory. Is either 'true' or 'false'.
#### `permission-needed` is used to specify a permission a player needs to open the menu.

### Items - section
#### `material` is the material of the item.
#### `display-name` is used to set the name of the item.
#### `amount` is used to set the amount of the item.
#### `slot` is used to set the slot of the item in the inventory.
#### `slots` is used to set multiple slots of the item in the inventory.
#### `lore` is used to set the lore of the item.
#### `enchantments` is used to set the enchantments of the item.
#### `itemflags` is used to set the itemflags of the item.
#### `skull-texture` is used to set the skull texture of the item, can only be used on items that have the material player heads.
#### `custom-model-data` is used to set the custom model data of the item.
#### `effect` is used to set the potion effect of the item, can only be used on items that have the material potion, splash_potion, lingering_potion or tipped_arrow.
#### `extended` is used to set if potion effect of the item is extended, can only be used on items that have a effect.
#### `upgraded` is used to set if potion effect of the item is upgraded, can only be used on items that have a effect.

### Events
#### `open-event` is the event called when a gui gets opened
#### `close-event` is the event called when a gui gets closed
#### `click-event` is the event called when a gui gets closed

### Events variables
#### `sound` is the sound played when a event is called - can be used in all events
#### `volume` is the volume of the sound - can be used in all events
#### `pitch` is the pitch of the sound - can be used in all events
#### `message` is the message sent to the player when a event is called - can be used in all events
#### `command-player` is the command the player will use when a event is called - can be used in all events
#### `command-console` is the command the console will use when a event is called - can be used in all events
#### `click-type` is the type of click needed for a click event to get called - only used in click-event
#### `allow-moving` if the clicked item can be moved around in the inventory - only used in click-event

## License
The ConfigUI plugin is licensed under GNU General Public License (GPL).