import React from "react";
import { UserOutlined } from "@ant-design/icons";
import { Button, Flex, Form, Input, InputNumber, Layout } from "antd";
import { useNavigate } from "react-router-dom";

const FindProductByIdFormContent: React.FC = () => {
  const navigate = useNavigate();
  const onFinish = (values: { id: number }) => {
    const id = values.id;
    // запрос
    console.log(values);
    console.log(id);
    navigate(`/users/${id}/show`, {
      state: { value: id },
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
            name="id"
            rules={[
              { required: true, message: "Please input id!" },
              { type: "number", message: "Input number!" },
            ]}
          >
            <InputNumber
              style={{ width: "100%" }}
              prefix={<UserOutlined className="site-form-item-icon" />}
              placeholder="Id"
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

export default FindProductByIdFormContent;
