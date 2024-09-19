# **JustRTP Developer Documentation**

## **Command: `/rtp`**

### Overview
The `/rtp` command randomly teleports a player to a solid block within the configured world and region, avoiding non-solid blocks such as water and air. The teleportation includes a countdown (default 5 seconds) during which the player is notified in the chat not to move. The command also displays the coordinates of the teleportation location in a title after teleportation.

---

### **Configuration Keys (`config.yml`)**

1. **`UseWorldBorder: boolean`**
   - **Default:** `false`
   - **Description:** If `true`, the teleportation will respect the world's defined world border, ignoring the `CenterX` and `CenterZ` values.

2. **`Biomes: List<String>`**
   - **Default:** `[]` (empty list allows teleportation to all biomes)
   - **Description:** List of biome names where players can be teleported. If the list is not empty, the player will only be teleported to allowed biomes. Use valid biome names from the [Spigot Javadocs](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/block/Biome.html).

3. **`MaxRadius: int`**
   - **Default:** `1000`
   - **Description:** The maximum radius from the center within which the player can be teleported.

4. **`MinRadius: int`**
   - **Default:** `10`
   - **Description:** The minimum radius from the center within which the player can be teleported.

5. **`CenterX: int`**
   - **Default:** `0`
   - **Description:** The X-coordinate of the teleportation center. Ignored if `UseWorldBorder` is `true`.

6. **`CenterZ: int`**
   - **Default:** `0`
   - **Description:** The Z-coordinate of the teleportation center. Ignored if `UseWorldBorder` is `true`.

7. **`Shape: String`**
   - **Default:** `'square'`
   - **Description:** Shape of the teleportation region. Supported shapes: `square`. Additional shapes can be implemented as needed.

8. **`MaxY: int`**
   - **Default:** `320`
   - **Description:** The maximum Y-coordinate for the teleportation location.

9. **`MinY: int`**
   - **Default:** `0`
   - **Description:** The minimum Y-coordinate for the teleportation location.

10. **`RTPOnDeath: boolean`**
    - **Default:** `false`
    - **Description:** If `true`, automatically teleports the player to a random location upon death.

11. **`RTPCountdown: int`**
    - **Default:** `5`
    - **Description:** Countdown time in seconds before the player is teleported.

---

### **Command Flow**

1. **Player Issues `/rtp` Command**
   - Only players can use the `/rtp` command; console and other non-player sources are restricted.

2. **Configuration Retrieval**
   - The plugin reads key values from the `config.yml` (e.g., `MaxRadius`, `MinRadius`, `CenterX`, `CenterZ`, `Biomes`, etc.).
   - It ensures that the teleportation coordinates are within the configured bounds and biome restrictions.

3. **Countdown Before Teleportation**
   - When the player uses `/rtp`, a red message is displayed in the chat: `Don't MOVE!!!`.
   - A countdown begins based on the `RTPCountdown` configuration (default is 5 seconds).
   - During each second of the countdown, the player sees a message like: `Teleporting in 5 seconds...`, `Teleporting in 4 seconds...`, etc.

4. **Finding a Suitable Solid Block**
   - The plugin ensures that the teleportation location is a solid block, checking the Y-axis from `MaxY` to `MinY`.
   - Non-solid blocks such as water and air are ignored.
   - The player is teleported only if a valid solid block is found at the random location.

5. **Teleportation**
   - After the countdown reaches zero, the player is teleported to the selected coordinates.
   - If the selected biome is not allowed (based on the `Biomes` config), the teleportation is canceled, and a message is displayed informing the player.
   - A title is shown to the player with the teleportation coordinates:
     - **Title:** "Teleported"
     - **Subtitle:** Coordinates (`X`, `Y`, `Z`) in **gold color**.

---

### **Key Methods in `RTPCommand.java`**

1. **`getRandomCoordinate(int minRadius, int maxRadius, String shape, Random random, int center)`**
   - **Description:** Generates a random X or Z coordinate based on the radius and center values. Currently supports the `'square'` shape.

2. **`getRandomSolidY(int minY, int maxY, World world, int x, int z)`**
   - **Description:** Finds a solid block between `minY` and `maxY`. If no valid solid block is found, defaults to `minY`.

3. **`BukkitRunnable` for Countdown**
   - **Description:** Manages the countdown process and handles player feedback in the chat. When the countdown reaches zero, it teleports the player to a valid solid block and sends a title message.

---

### **Permissions**

1. **`justrtp.use`**
   - **Default:** `true`
   - **Description:** Grants players permission to use the `/rtp` command. This can be customized in the `plugin.yml`.

---

### **Customizable Parameters**

1. **Countdown Time (`RTPCountdown`)**
   - The teleportation delay can be configured in the `config.yml`. Default is 5 seconds.
   
2. **Biomes**
   - Developers can restrict teleportation to specific biomes by listing them in the `Biomes` config section. If left empty, the plugin will teleport to any biome.

3. **Region Shape**
   - Currently, the plugin supports the `'square'` shape for teleportation regions. Additional shapes (e.g., circular) can be implemented by extending the logic in `getRandomCoordinate`.

---

### **Error Handling**

1. **Invalid Biomes:**
   - If the randomly chosen location falls within a biome not listed in the `Biomes` config, teleportation is canceled, and the player is informed.

2. **Non-Solid Blocks:**
   - If no valid solid block is found between the `MaxY` and `MinY` ranges, teleportation defaults to `MinY`.

---

### **Future Enhancements**
1. **Additional Region Shapes:**
   - Support for circular or other shapes can be added by modifying the coordinate generation logic.
   
2. **Biome-Specific Features:**
   - More advanced biome handling, such as prioritizing specific biome types, can be integrated.

---

### **Conclusion**

The `/rtp` command is a versatile tool for random teleportation in Minecraft, with configurable settings for biome restrictions, teleportation radii, countdown times, and more. It ensures that players are teleported to solid ground while offering developers flexibility through the `config.yml` file.

Let me know if you need any more details or adjustments!