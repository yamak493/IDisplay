package net.enabify.idisplay.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.component.ComponentChanges;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class IdisplayClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (!isShiftDown()) {
                return;
            }

            String itemId = Registries.ITEM.getId(stack.getItem()).toString();
            String componentText = formatComponentChanges(stack.getComponentChanges());

            lines.add(Text.literal(itemId).formatted(Formatting.DARK_GRAY));
            if (componentText != null) {
                lines.add(Text.literal(componentText).formatted(Formatting.GRAY));
            }
        });
    }

    private static boolean isShiftDown() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return false;
        }

        return InputUtil.isKeyPressed(client.getWindow(), InputUtil.GLFW_KEY_LEFT_SHIFT)
                || InputUtil.isKeyPressed(client.getWindow(), InputUtil.GLFW_KEY_RIGHT_SHIFT);
    }

    private static String formatComponentChanges(ComponentChanges changes) {
        if (ComponentChanges.EMPTY.equals(changes)) {
            return null;
        }

        String text = changes.toString();
        if (text.startsWith("{") && text.endsWith("}")) {
            return '[' + text.substring(1, text.length() - 1) + ']';
        }

        if (text.startsWith("[") && text.endsWith("]")) {
            return text;
        }

        return '[' + text + ']';
    }
}
