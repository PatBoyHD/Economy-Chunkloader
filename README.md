# Economy-Chunkloader
This is a little plugin, which allows players to forceload Chunks in exchange of a payment.

## Usage:
**You can't use /forceload add xy when using this plugin. As soon as the server is reloaded or restartet, all chunks which weren't forceloaded by the plugin will be un-forceloaded. That's what the admin commands are for!**

**You have some customizabillity via the config.yml:**

Bezahlung:  
    Item: NETHERITE_INGOT <- The Item which the player needs to pay  
    Anzahl: 1 <- total amount they need to pay  
Max-Chunks: 3 <- max chunks a player can have  
Max-Total-Chunks: 300 <- Count of forceloaded chunks there may be in the world (Admin Chunks do not count and can surpass that limit, stay safe)  

### Commands:  
- /load-chunk: forceloads the chunk the player is standing in, makes them the owner  
- /unload-chunk [confirm]: un-forceloads the chunk the player is standing in if they "own" it  
- /admin-load-chunk: forceloads the chunk the player is standing in, ignores Max-Total-Chunks, tho it warns when surpassing it  
- /admin-unload-chunk [confirm]: un-forceloads the chunk the player is standing in, ignores owner but gives the owner their chunk-load slot back  
- /admin-chunk-count: returns the total number of chunks in the form of total_chunks/Max-Total-Chunks  
