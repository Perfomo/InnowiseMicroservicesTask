import React, { useEffect, useState } from "react";
import { Flex, Layout } from "antd";
import LogoutButton from "../GeneralElements/LogoutButton";

const ProfileContent: React.FC = () => {
  const [userData, setUserData] = useState<any>([]);

  const fetchData = async () => {
    try {
      const response = await fetch(
        // 401
        "http://172.17.0.1:8081/users/api/users/" +
          localStorage.getItem("username"),
        {
          method: "GET",
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token"),
          },
        }
      );

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      console.log("data", data);
      setUserData(data);
    } catch (error) {
      console.error("Error during login: ", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <Layout style={{ height: "90vh", backgroundColor: "white" }}>
      <Flex
        justify="center"
        align="normal"
        style={{ height: "100vh", width: "100%", marginTop: "2%" }}
      >
        <table
          style={{
            width: "40%",
            height: "40%",
            textAlign: "center",
          }}
        >
          <tbody>
            <tr>
              <td
                style={{
                  border: "1px solid blue",
                  borderCollapse: "collapse",
                  width: "30",
                }}
              >
                Email
              </td>
              <td
                style={{ border: "1px solid blue", borderCollapse: "collapse" }}
              >
                {userData.email}
              </td>
            </tr>
            <tr>
              <td
                style={{ border: "1px solid blue", borderCollapse: "collapse" }}
              >
                Username
              </td>
              <td
                style={{ border: "1px solid blue", borderCollapse: "collapse" }}
              >
                {userData.username}
              </td>
            </tr>
            <tr>
              <td
                style={{ border: "1px solid blue", borderCollapse: "collapse" }}
              >
                First Name
              </td>
              <td
                style={{ border: "1px solid blue", borderCollapse: "collapse" }}
              >
                {userData.firstName}
              </td>
            </tr>
            <tr>
              <td
                style={{ border: "1px solid blue", borderCollapse: "collapse" }}
              >
                Last Name
              </td>
              <td
                style={{ border: "1px solid blue", borderCollapse: "collapse" }}
              >
                {userData.lastName}
              </td>
            </tr>
            <tr>
              <LogoutButton />
            </tr>
          </tbody>
        </table>
      </Flex>
    </Layout>
  );
};

export default ProfileContent;
