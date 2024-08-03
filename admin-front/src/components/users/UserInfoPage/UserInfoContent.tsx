import React, { CSSProperties, useEffect, useState } from "react";
import { Flex, Layout } from "antd";
import { useLocation, useNavigate } from "react-router-dom";
import TokenManager from "../../general/generalElements/TokenManager";
import BackButton from "../../general/generalElements/BackButton";
import UserOrdersButton from "../../general/generalElements/UserOrdersButton";
import DeleteButton from "../../general/generalElements/DeleteButton";
import ChangeInfoButton from "../../general/generalElements/ChangeInfoButton";

const UserInfoContent: React.FC = () => {
  const [userData, setUserData] = useState<any>([]);
  const [isNotFound, setIsNotFound] = useState<boolean>(false);
  const navigate = useNavigate();
  const location = useLocation();
  const { state } = location;
  const value = state?.value;

  const fetchData = async () => {
    try {
      const response = await fetch("/api/users/api/users/" + value, {
        method: "GET",
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
      });

      if (!response.ok) {
        if (response.status === 401) {
          try {
            TokenManager.tokenRefresh();
            window.location.reload();
          } catch (error) {
            console.log("Error during token refresh: " + error);
          }
        }
        if (response.status === 404) {
          setIsNotFound(true);
          return;
        }
        throw new Error("Network response was not ok");
      }

      const data = await response.json();
      setUserData(data);
    } catch (error) {
      localStorage.clear();
      console.error("Error during getting info: ", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  useEffect(() => {
    if (isNotFound) {
      navigate("/users/find");
    }
  }, [isNotFound, navigate]);

  const tdStyle: CSSProperties = {
    border: "1px solid blue",
    borderCollapse: "collapse",
    height: "45px",
  };

  const tdStyleButton: CSSProperties = {
    padding: "1%",
  };

  if (isNotFound) {
    return null;
  }

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
              <td style={tdStyle}>Username</td>
              <td style={tdStyle}>{userData.username}</td>
            </tr>
            <tr>
              <td style={tdStyle}>First Name</td>
              <td style={tdStyle}>{userData.firstName}</td>
            </tr>
            <tr>
              <td style={tdStyle}>Last Name</td>
              <td style={tdStyle}>{userData.lastName}</td>
            </tr>
          </tbody>
          <tfoot>
            <tr>
              <td style={tdStyleButton}>
                <BackButton />
              </td>
              <td style={tdStyleButton}>
                <UserOrdersButton username={String(userData.username)} />
              </td>
            </tr>
            <tr>
              <td style={{ width: "50%" }}>
                <DeleteButton
                  searchEl={String(userData.username)}
                  type="users"
                />
              </td>
              <td>
                <ChangeInfoButton searchEl={String(userData.username)} type="users"/>
              </td>
            </tr>
          </tfoot>
        </table>
      </Flex>
    </Layout>
  );
};

export default UserInfoContent;
