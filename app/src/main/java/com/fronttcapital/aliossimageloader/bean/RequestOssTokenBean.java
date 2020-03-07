package com.fronttcapital.aliossimageloader.bean;

public class RequestOssTokenBean {

    private AssumedRoleUser assumedRoleUser;
    private Credentials credentials;
    private String requestId;

    @Override
    public String toString() {
        return "RequestOssTokenBean{" +
                "assumedRoleUser=" + assumedRoleUser +
                ", credentials=" + credentials +
                ", requestId='" + requestId + '\'' +
                '}';
    }

    public AssumedRoleUser getAssumedRoleUser() {
        return assumedRoleUser;
    }

    public void setAssumedRoleUser(AssumedRoleUser assumedRoleUser) {
        this.assumedRoleUser = assumedRoleUser;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public class AssumedRoleUser {
        private String arn;
        private String assumedRoleId;

        @Override
        public String toString() {
            return "AssumedRoleUser{" +
                    "arn='" + arn + '\'' +
                    ", assumedRoleId='" + assumedRoleId + '\'' +
                    '}';
        }

        public String getArn() {
            return arn;
        }

        public void setArn(String arn) {
            this.arn = arn;
        }

        public String getAssumedRoleId() {
            return assumedRoleId;
        }

        public void setAssumedRoleId(String assumedRoleId) {
            this.assumedRoleId = assumedRoleId;
        }
    }

    public class Credentials {
        private String accessKeyId;
        private String accessKeySecret;
        private String expiration;
        private String securityToken;

        @Override
        public String toString() {
            return "Credentials{" +
                    "accessKeyId='" + accessKeyId + '\'' +
                    ", accessKeySecret='" + accessKeySecret + '\'' +
                    ", expiration='" + expiration + '\'' +
                    ", securityToken='" + securityToken + '\'' +
                    '}';
        }

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public String getSecurityToken() {
            return securityToken;
        }

        public void setSecurityToken(String securityToken) {
            this.securityToken = securityToken;
        }
    }
}
