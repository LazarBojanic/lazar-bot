from flask import Flask, send_file, request, jsonify
from PIL import Image, ImageDraw, ImageFont

app = Flask(__name__)

@app.route("/generate_keyboard", methods=["POST"])
def generate_keyboard_api():
    try:
        data = request.get_json()

        # Adjust the image dimensions
        width, height = 420, 220  # Increase width and height

        image = Image.new("RGB", (width, height), "white")
        draw = ImageDraw.Draw(image)

        key_size = 40
        key_spacing = 10
        rows = 3  # Number of rows for letters

        # Define colors for statuses
        colors = {
            "R": (209, 31, 52),  # Vibrant Red: #FF5733
            "Y": (232, 221, 16),  # Contemporary Yellow: #FFD633
            "G": (48, 219, 18),  # Fresh Green: #33FF57
        }

        # Define the font and font size
        font_size = 25
        font = ImageFont.truetype("GothicA1-Regular.ttf", font_size)

        # Calculate the number of keys in a row
        keys_per_row = width // (key_size + key_spacing)
        keys_in_current_row = 0

        # Initialize starting positions
        x = 10
        y = 10  # Start from the top

        for letterObj in data:
            color = colors.get(letterObj["status"], (0, 0, 0))
            draw.rectangle([x, y, x + key_size, y + key_size], fill=color, outline="black")
            text_x = x + (key_size - font_size) // 2
            text_y = y + (key_size - font_size) // 2
            draw.text((text_x, text_y), letterObj["letter"], fill="black", font=font)

            # Update positions for the next key
            x += key_size + key_spacing
            keys_in_current_row += 1

            # Check if we need to start a new row
            if keys_in_current_row >= keys_per_row:
                y += key_size + key_spacing  # Move to the next row
                x = 10  # Reset X position
                keys_in_current_row = 0

        # Save the image to a temporary file
        temp_filename = "temp_keyboard.png"
        image.save(temp_filename)

        # Send the generated image as a file in the response
        return send_file(temp_filename, mimetype="image/png")

    except Exception as e:
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host="localhost", port=5004)
