"""Thin wrapper over Mornye's provider-agnostic local library tools.

Only offline code is used: tag scanning and duplicate detection. No
download pipeline, no extension runtime, no network.
"""
from .api import find_duplicates, scan_library

__all__ = ["find_duplicates", "scan_library"]
