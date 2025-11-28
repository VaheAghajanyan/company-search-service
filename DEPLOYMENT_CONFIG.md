# Deployment Configuration Guide

## Changing Backend URL for Production

When deploying to a server, you only need to update **one file** to change the backend API URL.

### Configuration File Location

```
frontend/src/config/apiConfig.js
```

### How to Update

Open `frontend/src/config/apiConfig.js` and change the `BASE_URL`:

#### For Local Development (Current)
```javascript
export const API_CONFIG = {
    BASE_URL: 'http://localhost:8080',
    // ...
};
```

#### For Production Server
```javascript
export const API_CONFIG = {
    BASE_URL: 'http://your-server.com:8080',
    // or
    BASE_URL: 'https://api.your-domain.com',
    // ...
};
```

### Examples

**Example 1: Server with IP and Port**
```javascript
BASE_URL: 'http://192.168.1.100:8080'
```

**Example 2: Domain with HTTPS**
```javascript
BASE_URL: 'https://nomina-api.example.com'
```

**Example 3: Subdomain**
```javascript
BASE_URL: 'http://api.nomina.com'
```

**Example 4: Server with Different Port**
```javascript
BASE_URL: 'http://server.example.com:3000'
```

### What Gets Updated Automatically

Once you change `BASE_URL`, all API calls will automatically use the new URL:

✅ Text search: `/v1/aipa/search-by-name` and `/v1/wipo/search-by-name`
✅ Image search: `/api/images/compare-aipa` and `/api/images/compare-wipo`
✅ Refresh: `/v1/aipa/refresh` and `/v1/wipo/refresh`

### Deployment Steps

1. **Update the configuration**:
   ```bash
   # Edit frontend/src/config/apiConfig.js
   # Change BASE_URL to your production server URL
   ```

2. **Build the frontend**:
   ```bash
   cd frontend
   npm run build
   ```

3. **Deploy the `dist` folder** to your web server

4. **Make sure your backend is running** on the configured URL

### Testing

After deployment, verify the configuration:

1. Open browser developer tools (F12)
2. Go to Network tab
3. Perform a search
4. Check that requests are going to your production URL

### Troubleshooting

**Issue**: API calls fail after deployment

**Solutions**:
- ✅ Verify `BASE_URL` is correct in `apiConfig.js`
- ✅ Check backend server is running and accessible
- ✅ Verify CORS is configured on backend to allow frontend domain
- ✅ Check firewall/security groups allow traffic on the port
- ✅ Ensure HTTPS/HTTP protocol matches between frontend and backend

### CORS Configuration (Backend)

If your frontend and backend are on different domains, make sure your Spring Boot backend has CORS enabled:

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://your-frontend-domain.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}
```

---

## Quick Reference

| Environment | BASE_URL Example |
|------------|------------------|
| Local Development | `http://localhost:8080` |
| Production Server | `http://your-server.com:8080` |
| HTTPS Production | `https://api.your-domain.com` |
| Same Server | `http://localhost:8080` or `/api` (relative) |

**Remember**: Only change `BASE_URL` in `apiConfig.js` - all other files will use it automatically! 🎯
