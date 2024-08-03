import React from "react";
import { Flex, Form, Layout } from "antd";
import axios from "axios";
import { useLocation } from "react-router-dom";
import AllUserInfoForm from "../../general/generalElements/AllUserInfoForm";

const ChangeUserInfoForm: React.FC = () => {
  const [form] = Form.useForm();

  const location = useLocation();
  const { state } = location;
  const value = state?.value;

  const onFinish = (values: any) => {
    const { confirmPassword, ...requestData } = values;
    interface ErrorField {
      name: string;
      errors: string[];
    }
    if (values.username === value) {
      axios
        .put("/api/users/api/users/" + value, requestData, {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token"),
          },
        })
        .then(() => {
          form.resetFields();
          // add logic
        })
        .catch((error) => {
          if (error.response?.data) {
            const errorData = error.response.data;

            const errorFields: ErrorField[] = Object.entries(errorData).map(
              ([field, error]) => ({
                name: field,
                errors: Array.isArray(error) ? error : [error],
              })
            );
            console.log(errorFields);

            form.setFields(errorFields);
          }
        });
    } else {
      const error = "Username changing is not permitted";
      const inputData = [{ fieldName: "username", errorMessage: error }];

      const errorFields: ErrorField[] = inputData.map((item) => ({
        name: item.fieldName,
        errors: Array.isArray(error) ? error : [error],
      }));
      form.setFields(errorFields);
    }
  };

  return (
    <Layout style={{ height: "90vh", backgroundColor: "white" }}>
      <Flex
        justify="center"
        align="center"
        style={{ height: "100vh", width: "100%" }}
      >
        <AllUserInfoForm
          form={form}
          onFinish={onFinish}
          buttonText="Change info"
        />
      </Flex>
    </Layout>
  );
};

export default ChangeUserInfoForm;
