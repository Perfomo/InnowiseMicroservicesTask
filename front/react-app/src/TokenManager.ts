export default class TokenManager {

    public static async authenticate(values:any) {
        try {
            const response = await fetch('/realms/microServsRealm/protocol/openid-connect/token', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              body: new URLSearchParams({
                'client_id': 'frontClient',
                'grant_type': 'password',
                'username': values.username,
                'password': values.password,
                'client_secret': 'v9fDuQD5uVTuVSM9QSeWJ0JzS9nBIQFM'
              })
            });
      
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
      
            const data = await response.json();

            localStorage.setItem("token", data.access_token);
            localStorage.setItem("refToken", data.refresh_token);
            localStorage.setItem('username', values.username)
            
          } catch (error) {
              console.error('Error during login: ', error);
              throw(error);
          }
    }

    public static async tokenRefresh() {
      console.log("performTokenRefresh()")
      try {
        const response = await fetch('/realms/microServsRealm/protocol/openid-connect/token', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
          },
          body: new URLSearchParams({
            'client_id': 'frontClient',
            'grant_type': 'refresh_token',
            'refresh_token': localStorage.getItem("refToken") || '',
            'client_secret': 'v9fDuQD5uVTuVSM9QSeWJ0JzS9nBIQFM'
          })
        });
    
        if (!response.ok) {
          throw new Error('Failed to refresh token');
        }
    
        const data = await response.json();
        console.log("old token: " + localStorage.getItem("token"))
        localStorage.setItem("token", data.access_token)
        console.log("new token: " + localStorage.getItem("token"))
        console.log("Token refreshed")
        localStorage.setItem("refToken", data.refresh_token)    
      } catch (error) {
        console.error('Error refreshing token:', error);
        throw error;
      }
    }
}
  
  