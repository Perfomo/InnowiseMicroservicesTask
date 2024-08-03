import React from "react";
import { UserOutlined } from "@ant-design/icons";
import { Button, Flex, Form, Input, Layout } from "antd";
import { useNavigate } from "react-router-dom";

const FindUserContent: React.FC = () => {
  const navigate = useNavigate();
  const onFinish = (values: { username: string }) => {
    const username = values.username;
    console.log(values);
    console.log(username);
    navigate(`/users/${username}/show`, {
      state: { value: username },
    });
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
            rules={[{ required: true, message: "Please input username!" }]}
          >
            <Input
              prefix={<UserOutlined className="site-form-item-icon" />}
              placeholder="Username"
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="login-form-button"
              style={{ width: "100%" }}
            >
              Find
            </Button>
          </Form.Item>
        </Form>
      </Flex>
    </Layout>
  );
};

export default FindUserContent;
