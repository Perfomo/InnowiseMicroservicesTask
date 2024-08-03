import React, { CSSProperties, useEffect, useState } from "react";
import { Flex, Layout } from "antd";
import LogoutButton from "../GeneralElements/LogoutButton";
import TokenManager from "../../TokenManager";
import ChangeUserInfoButton from "../GeneralElements/ChangeUserInfoButton";
import DeleteUserButton from "../GeneralElements/DeleteUserButton";
import UserOrdersButton from "../GeneralElements/UserOrdersButton";

const ProfileContent: React.FC = () => {
  const [userData, setUserData] = useState<any>([]);

  const fetchData = async () => {
    try {
      const response = await fetch(
        "/users/api/users/" + localStorage.getItem("username"),
        {
          method: "GET",
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token"),
          },
        }
      );
      if (!response.ok) {
        if (response.status === 401) {
          try {
            TokenManager.tokenRefresh();
            window.location.reload();
          } catch (error) {
            console.log("error: " + error);
          }
        }
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      setUserData(data);

    } catch (error) {
      localStorage.clear();
      console.error("Error during login: ", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const tdStyle: CSSProperties = {
    border: "1px solid blue",
    borderCollapse: "collapse",
    height: "45px",
  };

  return (
    <Layout style={{ height: "100vh", backgroundColor: "white" }}>
      <h1 style={{ textAlign: "center", marginBottom: "0%", marginTop: "1%" }}>
        User info
      </h1>
      <Flex
        justify="center"
        align="normal"
        style={{ width: "100%", marginTop: "2%" }}
      >
        <table
          style={{
            width: "50%",
            height: "40%",
            textAlign: "center",
          }}
        >
          <tbody>
            <tr>
              <td style={tdStyle}>Email</td>
              <td style={tdStyle}>{userData.email}</td>
            </tr>
            <tr>
              <td
                style={tdStyle}
              >
                Username
              </td>
              <td
                style={tdStyle}
                >
                {userData.username}
              </td>
            </tr>
            <tr>
              <td
                style={tdStyle}
                >
                First Name
              </td>
              <td
                style={tdStyle}
                >
                {userData.firstName}
              </td>
            </tr>
            <tr>
              <td
                style={tdStyle}
                >
                Last Name
              </td>
              <td
                style={tdStyle}
                >
                {userData.lastName}
              </td>
            </tr>
          </tbody>
          <tfoot>
            <tr>
              <td style={{ width: "50%" }}>
                <LogoutButton />
              </td>
              <td>
                <UserOrdersButton />
              </td>
            </tr>
            <tr>
              <td style={{ width: "50%" }}>
                <DeleteUserButton />
              </td>
              <td>
                <ChangeUserInfoButton />
              </td>
            </tr>
          </tfoot>
        </table>
      </Flex>
    </Layout>
  );
};

export default ProfileContent;
