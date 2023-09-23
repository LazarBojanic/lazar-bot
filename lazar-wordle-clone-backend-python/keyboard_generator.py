from PIL import Image, ImageDraw, ImageFont

def generate_keyboard(letter_statuses):
    width, height = 300, 100
    image = Image.new("RGB", (width, height), "white")
    draw = ImageDraw.Draw(image)
    key_size = 40
    key_spacing = 10

    colors = {
        'R': (255, 0, 0),  # Red
        'Y': (255, 255, 0),  # Yellow
        'G': (0, 255, 0),  # Green
    }

    # Define the font and font size
    font_size = 20
    font = ImageFont.load_default()

    x = 10
    y = height // 2 - key_size // 2

    for letter, status in letter_statuses.items():
        color = colors.get(status, (0, 0, 0))
        draw.rectangle([x, y, x + key_size, y + key_size], fill=color, outline="black")
        
        # Calculate text size using the font
        text_size = 30
        text_width, text_height = text_size
        
        text_x = x + (key_size - text_width) // 2
        text_y = y + (key_size - text_height) // 2
        draw.text((text_x, text_y), letter, fill="white", font=font)
        x += key_size + key_spacing

    return image
