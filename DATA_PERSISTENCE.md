# Data Persistence Guide

## How Data Persistence Works

Your REST API Tester application uses **H2 file-based database** which means all your data is automatically saved to disk and persists across application restarts.

## Database Location

The database files are stored in the `data/` directory relative to where you run the application:

```
rest-api-tester/
└── data/
    ├── resttester.mv.db      (Main database file - contains all your data)
    └── resttester.trace.db   (Trace file - optional, for debugging)
```

## What Data is Stored

- ✅ **API Request History** - All executed requests with responses
- ✅ **Saved Requests** - Your request templates
- ✅ **Collections** - Your API collections and their requests

## Data Persistence Across Restarts

**Your data automatically persists!** When you:
1. Stop the application
2. Restart the application
3. All your saved requests, collections, and history will be available

The database file (`resttester.mv.db`) is automatically created on first run and updated every time you:
- Execute a request
- Save a request
- Create/update/delete a collection

## Backup Your Data

### Manual Backup

Simply copy the `data/` directory:

```bash
# From the project root directory
cp -r data/ data-backup-$(date +%Y%m%d)/
```

### Restore from Backup

```bash
# Stop the application first
# Then restore
cp -r data-backup-YYYYMMDD/ data/
# Restart the application
```

## Important Notes

1. **Database File Location**: The database is stored relative to where you run the application. If you run from different directories, the data will be in different locations.

2. **Don't Delete While Running**: Never delete or modify the database files while the application is running. Always stop the application first.

3. **Backup Regularly**: Make regular backups of the `data/` directory, especially if you have important collections or saved requests.

4. **Git Ignore**: The `data/` directory is in `.gitignore` to prevent committing database files to version control.

## Accessing the Database Directly

You can access the H2 console to view/manage data directly:

1. Start the application
2. Navigate to: `http://localhost:8080/h2-console`
3. Use these connection settings:
   - **JDBC URL**: `jdbc:h2:file:./data/resttester`
   - **Username**: `sa`
   - **Password**: (leave empty)

## Troubleshooting

### Data Not Persisting?

1. Check if the `data/` directory exists in your project root
2. Verify you're running the application from the project root directory
3. Check file permissions - ensure the application can write to the `data/` directory

### Database Corrupted?

1. Stop the application
2. Backup the current `data/` directory
3. Delete the corrupted database files
4. Restart the application (it will create a new database)
5. Restore from backup if needed

### Moving Data to Another Machine

1. Copy the entire `data/` directory
2. Place it in the same location on the new machine
3. Start the application - all your data will be available

