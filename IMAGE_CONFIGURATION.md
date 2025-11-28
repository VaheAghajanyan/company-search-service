# Image Search Feature - Configuration Guide

## 📁 Image Path Configuration

The frontend loads company logo images directly from your local file system using the configured root path.

### Configuration File

**Location**: `frontend/src/config/imageConfig.js`

```javascript
export const IMAGE_CONFIG = {
    // Root path where the 'image' folder is located
    ROOT_PATH: 'C:\\Users\\VaheA\\Desktop\\Nomina\\',
    
    // Helper function to get full image path
    getImagePath: (imagePath) => {
        if (!imagePath) return null;
        const cleanPath = imagePath.startsWith('/') || imagePath.startsWith('\\') 
            ? imagePath.substring(1) 
            : imagePath;
        return `file:///${IMAGE_CONFIG.ROOT_PATH}${cleanPath}`.replace(/\\/g, '/');
    }
};
```

### How It Works

1. **Backend returns** `imagePath` in the response, e.g., `"image\\wipo\\119\\1199849.jpg"`
2. **Frontend combines** `ROOT_PATH` + `imagePath` to create full path
3. **Result**: `file:///C:/Users/VaheA/Desktop/Nomina/image/wipo/119/1199849.jpg`
4. **Browser displays** the image from your local file system

### Example

**Backend Response**:
```json
{
  "id": 1199849,
  "markName": "Example Company",
  "imagePath": "image\\wipo\\119\\1199849.jpg",
  "link": "https://example.com",
  "perceptiveSimilarity": 0.95,
  "differenceSimilarity": 0.87
}
```

**Frontend Constructs**:
```
file:///C:/Users/VaheA/Desktop/Nomina/image/wipo/119/1199849.jpg
```

### Changing the Root Path

If you move the `image` folder or deploy to a different machine, simply update the `ROOT_PATH` in `imageConfig.js`:

```javascript
ROOT_PATH: 'D:\\Projects\\Nomina\\',  // New location
```

### Directory Structure Expected

```
C:\Users\VaheA\Desktop\Nomina\
└── image\
    ├── wipo\
    │   ├── 119\
    │   │   └── 1199849.jpg
    │   └── ...
    └── aipa\
        └── ...
```

### Browser Security Note

⚠️ **Important**: Modern browsers have security restrictions on accessing local files via `file://` protocol. 

**Solutions**:

1. **Development**: Use a local web server (which Vite already does)
2. **Production**: Serve images through the backend or use a static file server

If images don't load, you may need to:
- Serve images through a backend endpoint
- Use a static file server
- Configure CORS appropriately

### Alternative: Backend Image Serving

If `file://` protocol doesn't work, you can create a backend endpoint:

```java
@GetMapping("/api/images/serve")
public ResponseEntity<Resource> serveImage(@RequestParam String path) {
    try {
        String rootPath = "C:\\Users\\VaheA\\Desktop\\Nomina\\";
        Path imagePath = Paths.get(rootPath + path);
        Resource resource = new UrlResource(imagePath.toUri());
        
        if (resource.exists() && resource.isReadable()) {
            return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
        }
    } catch (Exception e) {
        // Handle error
    }
    return ResponseEntity.notFound().build();
}
```

Then update `imageConfig.js`:

```javascript
getImagePath: (imagePath) => {
    if (!imagePath) return null;
    return `http://localhost:8080/api/images/serve?path=${encodeURIComponent(imagePath)}`;
}
```

## 🎨 Features

- ✅ **Configurable root path** - Easy to change for different environments
- ✅ **Path normalization** - Handles both forward and backward slashes
- ✅ **Fallback placeholder** - Shows placeholder if image fails to load
- ✅ **Clean separation** - Configuration separate from component logic

## 📝 Summary

All image paths are now configured in one central location (`imageConfig.js`), making it easy to:
- Change the root directory
- Switch between local files and backend serving
- Deploy to different environments
- Maintain consistent image loading across the app
