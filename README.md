This tool inspired by [this thread](https://www.reddit.com/r/nvidia/comments/1ie7l1u/psa_how_to_enable_dlss_overrides_in_nvidia_app_on/)

The **DLSS Overrides** settings are disabled by default for "unsupported" applications. These settings are stored in a special file called **ApplicationStorage.json**, which is used by the **Nvidia App** and **Nvidia drivers**.
            
#### How the Program Works:
1. **Patch & Lock** — makes changes to the file to enable the DLSS Overrides settings and sets the file to read-only. This is necessary to prevent the **Nvidia App** from reverting the settings to their original state.
2. **Unlock** — makes the file editable. This is needed if you want to add a new application or automatically rescan games in the **Nvidia App**.

#### Modification Procedure:
- Press **Unlock** to make the file editable.
- Make the necessary changes in the **Nvidia App**.
- Press **Patch & Lock** again to lock in the changes.
- You may need to reconfigure games in the **Nvidia App** after applying the changes.

To apply the new settings, you will need to restart your computer, as the file is cached by the drivers.
