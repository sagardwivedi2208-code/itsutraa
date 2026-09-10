# IT SUTRAA - Production Launch Guide

## Recommended hosting for tonight: Railway + MySQL

Railway can run the Spring Boot app and provision MySQL in the same project. The app is Dockerized in this package.

### 1. Build locally before upload

From the project root:

```bash
./mvnw clean package -DskipTests
```

The Dockerfile expects:

`target/itsutraa-1.0.jar`

### 2. Push this project to GitHub

Create a private GitHub repository and push the complete `itsutraa` folder.

### 3. Railway

Create a Railway project and deploy the GitHub repository as a service.
Add a MySQL service to the same project.

Railway will expose MySQL variables such as:

- MYSQLHOST
- MYSQLPORT
- MYSQLUSER
- MYSQLPASSWORD
- MYSQLDATABASE
- MYSQL_URL

Set these application variables on the Spring Boot service:

- `DB_URL` = the Railway MySQL JDBC URL, e.g. `jdbc:mysql://<host>:<port>/<database>`
- `DB_USERNAME` = Railway MySQL user
- `DB_PASSWORD` = Railway MySQL password
- `DDL_AUTO` = `update` for first launch
- `APP_ENV` = `production`
- `COOKIE_SECURE` = `true`
- `APP_BASE_URL` = your public HTTPS URL
- `RAZORPAY_KEY_ID` = LIVE Razorpay Key ID
- `RAZORPAY_KEY_SECRET` = LIVE Razorpay Key Secret
- `MAIL_HOST` = SMTP host
- `MAIL_PORT` = `587`
- `MAIL_USERNAME` = your business email
- `MAIL_PASSWORD` = SMTP/App Password
- `MAIL_FROM` = your business email

Never commit these secrets to GitHub.

### 4. Persistent uploads

Admin course photos are stored under `/app/uploads`.
Attach a Railway Volume to the app service with mount path:

`/app/uploads`

Without persistent storage, uploaded admin photos can disappear when a container is replaced.

### 5. Custom domain

After the Railway service is live, add your domain in Railway service networking/settings and update DNS at your domain registrar using the DNS records Railway provides.

Then set:

`APP_BASE_URL=https://yourdomain.com`

### 6. Razorpay live

Use Razorpay Live keys only after your Razorpay account is approved for live payments. Razorpay recommends server-side signature verification and webhooks for reliable payment fulfillment.

Webhook endpoint for this app should be added only after the webhook handler is configured:

`https://yourdomain.com/payment/webhook`

### 7. Password recovery

Configure SMTP before launch. The reset link is 15 minutes and single-use. In production the application does not print reset links to the logs when mail fails.

### 8. Admin login

Default seeded admin:

- Email: `admin@itsutraa.com`
- Password: `ChangeMe123!`

Change this password immediately after the first production login.

### 9. First production checks

- `/health` returns `{"status":"UP","service":"IT SUTRAA"}`
- Register a new student
- Login/logout
- Forgot password email
- Admin login
- Add/edit course
- Upload a course image
- Test Razorpay with the correct mode/key
- Buy a course
- Open assessment
- Pass 7/10 or higher
- Download certificate PDF
- Verify certificate ID
- Test on mobile
- Confirm HTTPS

## Important production note

The current app uses a lightweight session-based authorization layer. Before scaling to a large public platform, migrate authorization to Spring Security roles, add CSRF protection, add rate limiting, add audit logging, add automated database backups, and move uploaded assets to durable object storage if upload volume grows.
