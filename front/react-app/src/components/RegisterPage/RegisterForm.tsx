import React from "react";
import { Flex, Form, Layout } from "antd";
import axios from "axios";
import AllUserInfoForm from "../GeneralElements/AllUserInfoForm";
import { useNavigate } from "react-router-dom";

const RegisterForm: React.FC = () => {
  const [form] = Form.useForm();
  const navigate = useNavigate();

  const onFinish = (values: any) => {
    const { confirmPassword, ...requestData } = values;

    axios.post("http://172.17.0.1:8081/users/api/users", requestData)
      .then(response => {
        form.resetFields();
        navigate("/login")
      })
      .catch(error => {

        if (error.response?.data) {
          const errorData = error.response.data;

          interface ErrorField {
            name: string;
            errors: string[];
          }
          
          const errorFields: ErrorField[] = Object.entries(errorData).map(([field, error]) => ({
            name: field,
            errors: Array.isArray(error) ? error : [error],
          }));
          
          form.setFields(errorFields);
        }
      });
  };

  return (
    <Layout style={{ height: "90vh", backgroundColor: "white" }}>
      <Flex
        justify="center"
        align="center"
        style={{ height: "100vh", width: "100%" }}
      >
      <AllUserInfoForm form={form} onFinish={onFinish} buttonText="Register" />
      </Flex>
    </Layout>
  );
};

export default RegisterForm;
