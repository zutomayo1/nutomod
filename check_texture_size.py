from PIL import Image

# 检查门的贴图尺寸
img = Image.open('src/main/resources/assets/nutonmod/textures/block/energy_door_bottom.png')
print(f'energy_door_bottom.png - Width: {img.width}, Height: {img.height}')
