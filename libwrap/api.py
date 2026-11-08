from __future__ import annotations

from dataclasses import asdict
from pathlib import Path
from typing import Callable

from mornye.core.library_dedup import scan_duplicates
from mornye.core.local_scanner import scan_path


def scan_library(root: str | Path, *, recursive: bool = True) -> list[dict]:
    """Read embedded tags of every supported audio file under `root`."""
    out = []
    for info in scan_path(root, recursive=recursive):
        row = asdict(info)
        row.pop("old_cover_base64", None)
        out.append(row)
    return out


def find_duplicates(
    root: str | Path,
    *,
    recursive: bool = True,
    match: str = "both",
    progress: Callable[[int, int, str], None] | None = None,
) -> dict:
    """Report duplicate groups and library stats. Never modifies files."""
    report = scan_duplicates(
        root, recursive=recursive, match=match, use_cache=False, progress=progress
    )
    return {
        "root": report.root,
        "stats": asdict(report.stats),
        "groups": [
            {
                "key": g.key,
                "matched_by": g.matched_by,
                "files": [f.path for f in g.files],
            }
            for g in report.groups
        ],
    }
