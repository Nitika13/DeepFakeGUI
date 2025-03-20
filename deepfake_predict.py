import torch
import torchvision.transforms as transforms
import sys
from PIL import Image
from detectors import DETECTOR  # Ensure LSDADetector is correctly imported

# Load the trained deepfake detection model
MODEL_PATH = "effnb4_best.pth"  # Update with your actual model path
config = {}  # Load model config if required

def load_model():
    model = DETECTOR['lsda'](config)  # Ensure 'lsda' is the correct model name
    model.load_state_dict(torch.load(MODEL_PATH, map_location="cuda" if torch.cuda.is_available() else "cpu"))
    model.eval()
    return model

model = load_model()

def preprocess_image(image_path, image_size=256):
    transform = transforms.Compose([
        transforms.Resize((image_size, image_size)),
        transforms.ToTensor(),
        transforms.Normalize(mean=[0.5, 0.5, 0.5], std=[0.5, 0.5, 0.5])
    ])
    image = Image.open(image_path).convert("RGB")
    return transform(image).unsqueeze(0)

def predict(image_path):
    image_tensor = preprocess_image(image_path)
    data_dict = {"image": image_tensor, "label": torch.tensor([0])}  # Dummy label

    with torch.no_grad():
        result = model.forward(data_dict, inference=True)

    prob_fake = result["prob"].item()
    return "Fake - AI Manipulated ❌" if prob_fake > 0.5 else "Real Image ✅"

if __name__ == "__main__":
    image_path = sys.argv[1]  # Get image path from Java
    prediction = predict(image_path)
    print(prediction)  # Java reads this output
