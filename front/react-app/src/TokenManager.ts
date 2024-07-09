export default class TokenManager {

    public static async authenticate(values:any) {
        try {
            const response = await fetch('http://172.17.0.1:8080/realms/microServsRealm/protocol/openid-connect/token', {
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
            console.log('Token received: ', data);

            localStorage.setItem("token", data.access_token);
            localStorage.setItem("refToken", data.refresh_token);
            localStorage.setItem('username', values.username)
            
          } catch (error) {
            console.error('Error during login: ', error);
          }
    }

    public static async tokenRefresh() {
        console.log("performTokenRefresh()")
        try {
          const response = await fetch('http://172.17.0.1:8080/realms/microServsRealm/protocol/openid-connect/token', {
            method: 'POST',
            headers: {
              'Authorization': 'Bearer ' + localStorage.getItem("refToken"),
              'Content-Type': 'application/json'
            },
          });
      
          if (!response.ok) {
            throw new Error('Failed to refresh token');
          }
      
          const data = await response.json();
      
          localStorage.setItem("token", data.access_token)
      
        } catch (error) {
          localStorage.clear()
          throw error;
        }
      }
  }
  
  