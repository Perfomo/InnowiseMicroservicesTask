import React from 'react';
import { LockOutlined, UserOutlined } from "@ant-design/icons";
import { Button, Flex, Form, Input } from "antd";
import Layout from "antd/es/layout/layout";
import { useNavigate } from 'react-router-dom';

const LoginForm: React.FC = () => {
  const navigate = useNavigate();

  const onFinish = async (values: any) => {
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

      localStorage.setItem('token', data.access_token);
      localStorage.setItem('username', values.username)

      navigate("/profile")

    } catch (error) {
      console.error('Error during login: ', error);
    }
  };



  return (
    <Layout style={{ height: "90vh", backgroundColor: "white" }}>
      <Flex
        justify="center"
        align="center"
        style={{ height: "100vh", width: "100%" }}
      >
        <Form
          name="normal_login"
          className="login-form"
          initialValues={{ remember: true }}
          onFinish={onFinish}
        >
          <Form.Item
            name="username"
            rules={[{ required: true, message: "Please input your Username!" }]}
          >
            <Input
              prefix={<UserOutlined className="site-form-item-icon" />}
              placeholder="Username"
            />
          </Form.Item>
          <Form.Item
            name="password"
            rules={[{ required: true, message: "Please input your Password!" }]}
          >
            <Input
              prefix={<LockOutlined className="site-form-item-icon" />}
              type="password"
              placeholder="Password"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="login-form-button"
              style={{ width: "100%" }}
            >
              Log in
            </Button>
          </Form.Item>
        </Form>
      </Flex>
    </Layout>
  );
};

export default LoginForm;
